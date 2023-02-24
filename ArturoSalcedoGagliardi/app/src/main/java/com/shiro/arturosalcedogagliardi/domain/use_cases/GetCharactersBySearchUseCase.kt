package com.shiro.arturosalcedogagliardi.domain.use_cases

import com.shiro.arturosalcedogagliardi.domain.model.CharacterResult

interface GetCharactersBySearchUseCase {
    suspend fun invoke(name: String, page: Int): Result<CharacterResult?>
}