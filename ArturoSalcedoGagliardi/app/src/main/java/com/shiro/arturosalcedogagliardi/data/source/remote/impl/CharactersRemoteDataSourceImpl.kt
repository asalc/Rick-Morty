package com.shiro.arturosalcedogagliardi.data.source.remote.impl

import com.shiro.arturosalcedogagliardi.data.model.CharacterResultRemote
import com.shiro.arturosalcedogagliardi.data.model.character.CharacterRemote
import com.shiro.arturosalcedogagliardi.data.source.remote.CharactersRemoteDataSource
import com.shiro.arturosalcedogagliardi.data.source.remote.api.CharactersApi
import javax.inject.Inject

class CharactersRemoteDataSourceImpl @Inject constructor(
    private val charactersApi: CharactersApi
): CharactersRemoteDataSource {
    override suspend fun getAllCharacters(page: Int): CharacterResultRemote? {
        return charactersApi.getCharacters(page).body()
    }

    override suspend fun getCharacterDetails(characterId: Int): CharacterRemote? {
        return charactersApi.getCharacterDetails(characterId).body()
    }

    override suspend fun searchCharacters(name: String, page: Int): CharacterResultRemote? {
        return charactersApi.searchCharacters(name, page).body()
    }
}