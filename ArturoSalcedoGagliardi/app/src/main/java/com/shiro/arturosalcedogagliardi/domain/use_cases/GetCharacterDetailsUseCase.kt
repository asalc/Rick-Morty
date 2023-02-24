package com.shiro.arturosalcedogagliardi.domain.use_cases

import com.shiro.arturosalcedogagliardi.domain.model.Character

interface GetCharacterDetailsUseCase {
    suspend fun invoke(characterId: Int): Result<Character?>
}