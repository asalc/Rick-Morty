package com.shiro.arturosalcedogagliardi.data.source.local.impl

import com.shiro.arturosalcedogagliardi.data.model.character.CharacterLocal
import com.shiro.arturosalcedogagliardi.data.source.local.CharactersLocalDataSource
import com.shiro.arturosalcedogagliardi.data.source.local.database.CharactersDao
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
                        id = character.id
                        name = character.name
                        status = remoteCharacter.status
                        species = character.species
                        type = remoteCharacter.type
                        gender = remoteCharacter.gender
                        origin = character.origin
                        location = character.location
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
}