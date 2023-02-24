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

    override suspend fun getCharacterDetails(characterId: Int): CharacterLocal {
        return charactersDao.getCharacterDetails(characterId)
    }

    override suspend fun searchCharacters(name: String): List<CharacterLocal> {
        return charactersDao.searchCharacters(name)
    }

    override suspend fun saveCharacter(character: CharacterLocal) {
        charactersDao.insertCharacter(character)
    }
}