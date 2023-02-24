package com.shiro.arturosalcedogagliardi.domain.use_cases.impl

import com.shiro.arturosalcedogagliardi.domain.model.Character
import com.shiro.arturosalcedogagliardi.domain.repository.CharactersRepository
import com.shiro.arturosalcedogagliardi.domain.use_cases.GetCharacterDetailsUseCase
import javax.inject.Inject

class GetCharacterDetailsUseCaseImpl @Inject constructor(
    private val charactersRepository: CharactersRepository
): GetCharacterDetailsUseCase {
    override suspend operator fun invoke(characterId: Int): Result<Character?> {
        return charactersRepository.getCharacterDetails(characterId)
    }
}