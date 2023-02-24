package com.shiro.arturosalcedogagliardi.data.source.local

import com.shiro.arturosalcedogagliardi.data.model.character.CharacterLocal

interface CharactersLocalDataSource {
    suspend fun getAllCharacters(): List<CharacterLocal>
    suspend fun getCharacterDetails(characterId: Int): CharacterLocal
    suspend fun searchCharacters(name: String): List<CharacterLocal>
    suspend fun saveCharacter(character: CharacterLocal)
}