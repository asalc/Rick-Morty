package com.shiro.arturosalcedogagliardi.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shiro.arturosalcedogagliardi.App
import com.shiro.arturosalcedogagliardi.data.source.remote.api.ApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import com.shiro.arturosalcedogagliardi.domain.model.Character
import com.shiro.arturosalcedogagliardi.domain.model.Pager
import com.shiro.arturosalcedogagliardi.domain.use_cases.GetAllCharactersUseCase
import com.shiro.arturosalcedogagliardi.domain.use_cases.GetCharactersBySearchUseCase
import com.shiro.arturosalcedogagliardi.helpers.extensions.isNetworkAvailable
import com.shiro.arturosalcedogagliardi.helpers.extensions.parseException
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllCharactersUseCase: GetAllCharactersUseCase,
    private val getCharactersBySearchUseCase: GetCharactersBySearchUseCase
) : ViewModel() {

    private val isLoading = MutableLiveData<Boolean>()
    private val searchQuery = MutableLiveData<String>()
    private val charactersList = MutableLiveData<ArrayList<Character>>()
    private val errorResourceId = MutableLiveData<Int>()

    private var page: Int = 1
    private var hasNext: Boolean = true
    private var resetList: Boolean = false

    fun getPage(): Int = page
    fun hastNext(): Boolean = hasNext
    fun isSearch(): Boolean = !searchQuery.value.isNullOrEmpty()

    fun refreshData() {

    }

    fun setSearchQuery(query: String, submit: Boolean = false) {
        searchQuery.value = query
        if (submit)
            refreshData()
    }

    fun resetQuery() { searchQuery.value = "" }
    fun clearError() { errorResourceId.value = 0 }

    fun getCharacters() {
        if (App.instance.isNetworkAvailable) {
            isLoading.value = true
            viewModelScope.launch {
                getAllCharactersUseCase(page)
                    .onSuccess { characterResult ->
                        characterResult?.results?.let { setCharactersList(it) }
                        characterResult?.info?.let { setPage(it) }
                    }
                    .onFailure {
                        val exception = it as? Exception
                        val apiError = exception?.parseException()
                        errorResourceId.value = apiError?.errorMessage
                    }
            }
        } else {
            errorResourceId.value = ApiError.Network().errorMessage
        }
    }

    fun getCharactersBySearch() {

    }

    private fun endPage() { page = 0 }
    private fun resetPage() { page = 1 }
    private fun setPage(pager: Pager) {
        if (pager.next == null) {
            endPage()
            hasNext = false
        } else {
            page += 1
        }
    }

    private fun setCharactersList(characters: List<Character>) {
        val auxCharactersList =
            if (resetList) arrayListOf()
            else charactersList.value
        auxCharactersList?.addAll(characters)
        charactersList.value = auxCharactersList
    }
}