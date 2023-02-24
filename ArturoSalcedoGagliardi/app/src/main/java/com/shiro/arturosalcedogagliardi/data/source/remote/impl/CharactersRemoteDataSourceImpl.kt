package com.shiro.arturosalcedogagliardi.data.source.remote.impl

import com.shiro.arturosalcedogagliardi.data.model.CharacterResultRemote
import com.shiro.arturosalcedogagliardi.data.model.character.CharacterRemote
import com.shiro.arturosalcedogagliardi.data.source.remote.CharactersRemoteDataSource
import com.shiro.arturosalcedogagliardi.data.source.remote.api.ApiError
import com.shiro.arturosalcedogagliardi.data.source.remote.api.CharactersApi
import com.shiro.arturosalcedogagliardi.helpers.extensions.callResult
import com.shiro.arturosalcedogagliardi.helpers.extensions.parseErrorCode
import javax.inject.Inject

class CharactersRemoteDataSourceImpl @Inject constructor(
    private val charactersApi: CharactersApi
): CharactersRemoteDataSource {
    override suspend fun getAllCharacters(page: Int): Result<CharacterResultRemote?> {
        return callResult { charactersApi.getCharacters(page) }
    }

    override suspend fun getCharacterDetails(characterId: Int): Result<CharacterRemote?> {
        return callResult { charactersApi.getCharacterDetails(characterId) }
    }

    override suspend fun searchCharacters(name: String, page: Int): Result<CharacterResultRemote?> {
        return callResult { charactersApi.searchCharacters(name, page) }
    }
}