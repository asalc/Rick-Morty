package com.shiro.arturosalcedogagliardi.domain.use_cases.impl

import com.shiro.arturosalcedogagliardi.domain.model.Character
import com.shiro.arturosalcedogagliardi.domain.repository.CharactersRepository
import com.shiro.arturosalcedogagliardi.domain.use_cases.UpdateCharacterUseCase
import javax.inject.Inject

class UpdateCharacterUseCaseImpl @Inject constructor(
    private val charactersRepository: CharactersRepository
): UpdateCharacterUseCase {

    override suspend operator fun invoke(character: Character): Result<Boolean> {
        return charactersRepository.updateCharacter(character)
    }
}