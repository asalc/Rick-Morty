package com.shiro.arturosalcedogagliardi.data.source.local.impl

import com.shiro.arturosalcedogagliardi.data.mappers.toLocal
import com.shiro.arturosalcedogagliardi.data.model.character.CharacterLocal
import com.shiro.arturosalcedogagliardi.data.source.local.CharactersLocalDataSource
import com.shiro.arturosalcedogagliardi.data.source.local.database.CharactersDao
import com.shiro.arturosalcedogagliardi.domain.model.Character
import com.shiro.arturosalcedogagliardi.helpers.extensions.callResult
import com.shiro.arturosalcedogagliardi.helpers.extensions.parseException
import javax.inject.Inject

class CharactersLocalDataSourceImpl @Inject constructor(
    private val charactersDao: CharactersDao
): CharactersLocalDataSource {
    override suspend fun getAllCharacters(): List<CharacterLocal> {
        return charactersDao.getAllCharacters()
    }

    override suspend fun getCharacterDetails(characterId: Int): CharacterLocal? {
        return charactersDao.getCharacterDetails(characterId)
    }

    override suspend fun saveCharacter(character: CharacterLocal) {
        charactersDao.insertCharacter(character)
    }

    override suspend fun updateCharacter(
        remoteCharacter: CharacterLocal
    ) {
        remoteCharacter.id?.let {
            getCharacterDetails(it)?.let { character ->
                charactersDao.updateCharacter(
                    character.apply {
                        status = remoteCharacter.status
                        type = remoteCharacter.type
                        gender = remoteCharacter.gender
                        image = remoteCharacter.image
                        url = remoteCharacter.image
                        created = remoteCharacter.created
                    }
                )
            } ?: run {
                saveCharacter(remoteCharacter)
            }
        }
    }

    override suspend fun updateCharacterFromDomain(
        character: CharacterLocal
    ): Result<Boolean> {
        return try {
            character.id?.let {
                getCharacterDetails(it)?.let { characterLocal ->
                    charactersDao.updateCharacter(
                        characterLocal.apply {
                            name = character.name
                            origin = character.origin
                            location = character.location
                        }
                    )
                } ?: run {
                    saveCharacter(character)
                }
            } ?: run {
                saveCharacter(character)
            }
            Result.success(true)
        } catch (exception: Exception) {
            Result.failure(exception.parseException())
        }
    }
}