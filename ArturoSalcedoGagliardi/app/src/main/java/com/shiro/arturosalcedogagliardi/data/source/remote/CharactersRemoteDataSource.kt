package com.shiro.arturosalcedogagliardi.data.source.remote

import com.shiro.arturosalcedogagliardi.data.model.CharacterResultRemote
import com.shiro.arturosalcedogagliardi.data.model.character.CharacterRemote

interface CharactersRemoteDataSource {
    suspend fun getAllCharacters(page: Int): CharacterResultRemote?
    suspend fun getCharacterDetails(characterId: Int): CharacterRemote?
    suspend fun searchCharacters(name: String, page: Int): CharacterResultRemote?
}