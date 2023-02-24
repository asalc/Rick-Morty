package com.shiro.arturosalcedogagliardi.domain.use_cases

import com.shiro.arturosalcedogagliardi.domain.model.CharacterResult

interface GetAllCharactersUseCase {
    suspend fun invoke(page: Int): Result<CharacterResult?>
}