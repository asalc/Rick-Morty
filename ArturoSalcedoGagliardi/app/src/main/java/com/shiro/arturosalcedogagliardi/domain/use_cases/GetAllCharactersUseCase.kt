package com.shiro.arturosalcedogagliardi.domain.use_cases

import com.shiro.arturosalcedogagliardi.domain.model.CharacterResult

interface GetAllCharactersUseCase {
    suspend operator fun invoke(page: Int): Result<CharacterResult?>
}