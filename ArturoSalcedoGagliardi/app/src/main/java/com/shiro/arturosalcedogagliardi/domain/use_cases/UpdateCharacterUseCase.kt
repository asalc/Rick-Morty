package com.shiro.arturosalcedogagliardi.domain.use_cases

import com.shiro.arturosalcedogagliardi.domain.model.Character

interface UpdateCharacterUseCase {
    suspend operator fun invoke(character: Character): Result<Boolean>
}