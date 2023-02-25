package com.shiro.arturosalcedogagliardi.data.source.local

import com.shiro.arturosalcedogagliardi.data.model.character.CharacterLocal

interface CharactersLocalDataSource {
    suspend fun getAllCharacters(): List<CharacterLocal>
    suspend fun getCharacterDetails(characterId: Int): CharacterLocal?
    suspend fun saveCharacter(character: CharacterLocal)
    suspend fun updateCharacter(remoteCharacter: CharacterLocal)
}