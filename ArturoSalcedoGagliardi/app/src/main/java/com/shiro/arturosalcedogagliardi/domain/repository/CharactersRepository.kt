package com.shiro.arturosalcedogagliardi.domain.repository

import com.shiro.arturosalcedogagliardi.domain.model.Character
import com.shiro.arturosalcedogagliardi.domain.model.CharacterResult

interface CharactersRepository {
    suspend fun getAllCharacters(page: Int): Result<CharacterResult?>
    suspend fun getCharacterDetails(characterId: Int): Result<Character?>
    suspend fun updateCharacter(character: Character): Result<Boolean>
    suspend fun deleteCharacter(character: Character): Result<Int?>
}