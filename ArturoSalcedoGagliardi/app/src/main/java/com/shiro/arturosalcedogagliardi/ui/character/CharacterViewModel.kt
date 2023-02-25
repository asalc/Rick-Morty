package com.shiro.arturosalcedogagliardi.ui.character

import android.os.Build
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shiro.arturosalcedogagliardi.data.source.remote.api.ApiError
import com.shiro.arturosalcedogagliardi.domain.model.Character
import com.shiro.arturosalcedogagliardi.domain.use_cases.UpdateCharacterUseCase
import com.shiro.arturosalcedogagliardi.helpers.Constants
import com.shiro.arturosalcedogagliardi.helpers.extensions.parseException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val updateCharacterUseCase: UpdateCharacterUseCase
): ViewModel() {

    val character = MutableLiveData<Character>()
    val characterError = MutableLiveData<Int>()
    val characterUpdateSuccessful = MutableLiveData<Boolean>()

    fun checkIntent(bundle: Bundle?) {
        bundle?.let {
            character.value =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    it.getSerializable(Constants.CHARACTER, Character::class.java)
                else it.getSerializable(Constants.CHARACTER) as? Character
            if (character.value == null)
                characterError.value = ApiError.NotFound().errorMessage
        } ?: run {
            characterError.value = ApiError.NotFound().errorMessage
        }
    }

    fun saveChanges() {
        character.value?.let {
            viewModelScope.launch(Dispatchers.IO) {
                updateCharacterUseCase(it)
                    .onSuccess { isSuccess ->
                        withContext(Dispatchers.Main) {
                            characterUpdateSuccessful.value = isSuccess
                        }
                    }
                    .onFailure {
                        withContext(Dispatchers.Main) {
                            characterError.value =
                                if (it is ApiError) it.errorMessage
                                else ApiError.Unknown().errorMessage
                        }
                    }
            }
        }
    }
}