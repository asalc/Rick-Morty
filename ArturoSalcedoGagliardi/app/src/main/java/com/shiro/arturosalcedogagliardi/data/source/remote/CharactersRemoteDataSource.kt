package com.shiro.arturosalcedogagliardi.data.source.remote

import com.shiro.arturosalcedogagliardi.data.model.CharacterResultRemote
import com.shiro.arturosalcedogagliardi.data.model.character.CharacterRemote

interface CharactersRemoteDataSource {
    suspend fun getAllCharacters(page: Int): Result<CharacterResultRemote?>
    suspend fun getCharacterDetails(characterId: Int): Result<CharacterRemote?>
}