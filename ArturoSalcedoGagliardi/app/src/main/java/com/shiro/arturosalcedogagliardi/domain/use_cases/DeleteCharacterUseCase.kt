package com.shiro.arturosalcedogagliardi.domain.use_cases

import com.shiro.arturosalcedogagliardi.domain.model.Character

interface DeleteCharacterUseCase {
    suspend operator fun invoke(character: Character): Result<Int?>
}