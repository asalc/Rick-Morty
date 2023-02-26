package com.shiro.arturosalcedogagliardi.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shiro.arturosalcedogagliardi.data.source.remote.api.ApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import com.shiro.arturosalcedogagliardi.domain.model.Character
import com.shiro.arturosalcedogagliardi.domain.model.Pager
import com.shiro.arturosalcedogagliardi.domain.use_cases.DeleteCharacterUseCase
import com.shiro.arturosalcedogagliardi.domain.use_cases.GetAllCharactersUseCase
import com.shiro.arturosalcedogagliardi.helpers.extensions.parseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllCharactersUseCase: GetAllCharactersUseCase,
    private val deleteCharacterUseCase: DeleteCharacterUseCase
) : ViewModel() {

    val isLoading = MutableLiveData<Boolean>()
    val charactersList = MutableLiveData<ArrayList<Character>>()
    val deletedCharacterId = MutableLiveData<Int>()
    val errorResourceId = MutableLiveData<Int>()

    private var page: Int = 1
    private var hasNext: Boolean = true
    private var resetList: Boolean = false

    fun hastNext(): Boolean = hasNext

    fun refreshData() {
        resetPage()
        resetList = true
        getCharacters()
    }

    fun clearError() { errorResourceId.value = 0 }

    fun getCharacters() {
        isLoading.value = true
        viewModelScope.launch {
            getAllCharactersUseCase(page)
                .onFailure {
                    parseFailure(it)
                    endPage()
                }
                .onSuccess { characterResult ->
                    characterResult?.results?.let { setCharactersList(it) }
                    characterResult?.info?.let {
                        setPage(it)
                    }
                }
            isLoading.value = false
            resetList = false
        }
    }

    fun deleteCharacter(character: Character) {
        isLoading.value = true
        viewModelScope.launch {
            deleteCharacterUseCase(character)
                .onFailure { parseFailure(it) }
                .onSuccess { result ->
                    result?.let {
                        deletedCharacterId.value = it
                    } ?: run {
                        parseFailure(ApiError.Unknown())
                    }
                }
            isLoading.value = false
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

    private fun setCharactersList(characters: List<Character>) {
        val auxCharactersList: ArrayList<Character> =
            if (resetList) arrayListOf()
            else charactersList.value ?: arrayListOf()
        auxCharactersList.addAll(characters)
        charactersList.value = auxCharactersList
    }

    private fun parseFailure(throwable: Throwable) {
        val exception = throwable as? Exception
        val apiError = exception?.parseException()
        errorResourceId.value = apiError?.errorMessage
    }
}