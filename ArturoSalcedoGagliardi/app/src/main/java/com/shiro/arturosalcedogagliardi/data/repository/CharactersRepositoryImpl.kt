package com.shiro.arturosalcedogagliardi.data.repository

import com.shiro.arturosalcedogagliardi.data.mappers.toCharacterResult
import com.shiro.arturosalcedogagliardi.data.mappers.toDomain
import com.shiro.arturosalcedogagliardi.data.mappers.toLocal
import com.shiro.arturosalcedogagliardi.data.source.local.CharactersLocalDataSource
import com.shiro.arturosalcedogagliardi.data.source.remote.CharactersRemoteDataSource
import com.shiro.arturosalcedogagliardi.data.source.remote.api.ApiError
import com.shiro.arturosalcedogagliardi.domain.model.Character
import com.shiro.arturosalcedogagliardi.domain.model.CharacterResult
import com.shiro.arturosalcedogagliardi.domain.repository.CharactersRepository
import com.shiro.arturosalcedogagliardi.helpers.Constants
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    private val charactersLocalDataSource: CharactersLocalDataSource,
    private val charactersRemoteDataSource: CharactersRemoteDataSource
): CharactersRepository {
    override suspend fun getAllCharacters(page: Int): Result<CharacterResult?> {
        val response = charactersRemoteDataSource.getAllCharacters(page)
        return if (response.isSuccess) {
            response.getOrNull()?.results?.forEach {
                charactersLocalDataSource.updateCharacter(
                    it.toLocal()
                )
            }
            Result.success(
                charactersLocalDataSource.getAllCharacters()
                    .subList(
                        (page - 1) * Constants.CHARACTERS_PER_PAGE,
                        page * Constants.CHARACTERS_PER_PAGE
                    )
                    .toCharacterResult().apply {
                        info = response.getOrNull()?.info
                    }
            )
        } else {
            val exception = response.exceptionOrNull()
            if (exception is ApiError) {
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
                localResponse?.toDomain()?.let {
                    Result.success(it)
                } ?: run {
                    Result.failure(ApiError.NotFound())
                }
            } else {
                Result.failure(exception ?: Exception())
            }
        }
    }

    override suspend fun updateCharacter(character: Character): Result<Boolean> {
        return charactersLocalDataSource.updateCharacterFromDomain(character.toLocal()).map { it ?: false }
    }
}