package com.shiro.arturosalcedogagliardi.domain.use_cases.impl

import com.shiro.arturosalcedogagliardi.domain.model.CharacterResult
import com.shiro.arturosalcedogagliardi.domain.repository.CharactersRepository
import com.shiro.arturosalcedogagliardi.domain.use_cases.GetCharactersBySearchUseCase
import javax.inject.Inject

class GetCharactersBySearchUseCaseImpl @Inject constructor(
    private val charactersRepository: CharactersRepository
): GetCharactersBySearchUseCase {
    override suspend operator fun invoke(name: String, page: Int): Result<CharacterResult?> {
        return charactersRepository.searchCharacters(name, page)
    }
}