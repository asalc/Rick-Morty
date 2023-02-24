package com.shiro.arturosalcedogagliardi.data.repository

import com.shiro.arturosalcedogagliardi.data.mappers.toCharacterResult
import com.shiro.arturosalcedogagliardi.data.mappers.toDomain
import com.shiro.arturosalcedogagliardi.data.source.local.CharactersLocalDataSource
import com.shiro.arturosalcedogagliardi.data.source.remote.CharactersRemoteDataSource
import com.shiro.arturosalcedogagliardi.data.source.remote.api.ApiError
import com.shiro.arturosalcedogagliardi.domain.model.Character
import com.shiro.arturosalcedogagliardi.domain.model.CharacterResult
import com.shiro.arturosalcedogagliardi.domain.repository.CharactersRepository
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    private val charactersLocalDataSource: CharactersLocalDataSource,
    private val charactersRemoteDataSource: CharactersRemoteDataSource
): CharactersRepository {
    override suspend fun getAllCharacters(page: Int): Result<CharacterResult?> {
        val response = charactersRemoteDataSource.getAllCharacters(page)
        return if (response.isSuccess) {
            response.map {
                it?.toDomain()
            }
        } else {
            val exception = response.exceptionOrNull()
            if (exception is ApiError.Network) {
                val localResponse = charactersLocalDataSource.getAllCharacters()
                Result.success(localResponse.toCharacterResult())
            } else {
                Result.failure(exception ?: Exception())
            }
        }
    }

    override suspend fun getCharacterDetails(characterId: Int): Result<Character?> {
        val response = charactersRemoteDataSource.getCharacterDetails(characterId)
        return if (response.isSuccess) {
            response.map {
                it?.toDomain()
            }
        } else {
            val exception = response.exceptionOrNull()
            if (exception is ApiError.Network) {
                val localResponse = charactersLocalDataSource.getCharacterDetails(characterId)
                Result.success(localResponse.toDomain())
            } else {
                Result.failure(exception ?: Exception())
            }
        }
    }

    override suspend fun searchCharacters(name: String, page: Int): Result<CharacterResult?> {
        val response = charactersRemoteDataSource.searchCharacters(name, page)
        return if (response.isSuccess) {
            response.map {
                it?.toDomain()
            }
        } else {
            val exception = response.exceptionOrNull()
            if (exception is ApiError.Network) {
                val localResponse = charactersLocalDataSource.searchCharacters(name)
                Result.success(localResponse.toCharacterResult())
            } else {
                Result.failure(exception ?: Exception())
            }
        }
    }
}