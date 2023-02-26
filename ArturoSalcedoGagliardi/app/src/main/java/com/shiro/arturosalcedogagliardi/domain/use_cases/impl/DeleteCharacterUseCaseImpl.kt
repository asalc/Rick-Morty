package com.shiro.arturosalcedogagliardi.domain.use_cases.impl

import com.shiro.arturosalcedogagliardi.domain.model.Character
import com.shiro.arturosalcedogagliardi.domain.repository.CharactersRepository
import com.shiro.arturosalcedogagliardi.domain.use_cases.DeleteCharacterUseCase
import javax.inject.Inject

class DeleteCharacterUseCaseImpl @Inject constructor(
    private val charactersRepository: CharactersRepository
): DeleteCharacterUseCase {

    override suspend operator fun invoke(character: Character): Result<Int?> {
        return charactersRepository.deleteCharacter(character)
    }
}