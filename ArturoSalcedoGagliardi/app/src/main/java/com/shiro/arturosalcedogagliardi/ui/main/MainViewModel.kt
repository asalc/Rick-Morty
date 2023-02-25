package com.shiro.arturosalcedogagliardi.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.shiro.arturosalcedogagliardi.domain.model.Character
import com.shiro.arturosalcedogagliardi.domain.model.Pager
import com.shiro.arturosalcedogagliardi.domain.use_cases.GetAllCharactersUseCase
import com.shiro.arturosalcedogagliardi.helpers.extensions.parseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllCharactersUseCase: GetAllCharactersUseCase,
) : ViewModel() {

    val isLoading = MutableLiveData<Boolean>()
    val charactersList = MutableLiveData<ArrayList<Character>>()
    val errorResourceId = MutableLiveData<Int>()

    private var page: Int = 1
    private var hasNext: Boolean = true
    private var resetList: Boolean = false

    fun getPage(): Int = page
    fun hastNext(): Boolean = hasNext
    fun isReset(): Boolean = resetList

    fun refreshData() {
        resetPage()
        resetList = true
        getCharacters()
    }

    fun clearError() { errorResourceId.value = 0 }

    fun getCharacters() {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            getAllCharactersUseCase(page)
                .onFailure { parseFailure(it) }
                .onSuccess { characterResult ->
                    characterResult?.results?.let { setCharactersList(it) }
                    characterResult?.info?.let {
                        setPage(it)
                    }
                }
            withContext(Dispatchers.Main) {
                isLoading.value = false
                resetList = false
            }
        }
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

    private suspend fun setCharactersList(characters: List<Character>) {
        withContext(Dispatchers.Main) {
            val auxCharactersList: ArrayList<Character> =
                if (resetList) arrayListOf()
                else charactersList.value ?: arrayListOf()
            auxCharactersList.addAll(characters)
            charactersList.value = auxCharactersList
        }
    }

    private suspend fun parseFailure(throwable: Throwable) {
        withContext(Dispatchers.Main) {
            val exception = throwable as? Exception
            val apiError = exception?.parseException()
            errorResourceId.value = apiError?.errorMessage
            endPage()
        }
    }
}