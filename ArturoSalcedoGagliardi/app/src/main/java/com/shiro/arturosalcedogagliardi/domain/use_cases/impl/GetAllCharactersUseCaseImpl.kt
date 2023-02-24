package com.shiro.arturosalcedogagliardi.domain.use_cases.impl

import com.shiro.arturosalcedogagliardi.domain.model.CharacterResult
import com.shiro.arturosalcedogagliardi.domain.repository.CharactersRepository
import com.shiro.arturosalcedogagliardi.domain.use_cases.GetAllCharactersUseCase
import javax.inject.Inject

class GetAllCharactersUseCaseImpl @Inject constructor(
    private val charactersRepository: CharactersRepository
): GetAllCharactersUseCase {

    override suspend operator fun invoke(page: Int): Result<CharacterResult?> {
        return charactersRepository.getAllCharacters(page)
    }
}