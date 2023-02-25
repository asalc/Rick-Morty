package com.shiro.arturosalcedogagliardi.data.source.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shiro.arturosalcedogagliardi.data.model.character.CharacterLocal

@Dao
interface CharactersDao {
    @Query("SELECT * FROM CharacterLocal")
    fun getAllCharacters(): List<CharacterLocal>

    @Query("SELECT * FROM CharacterLocal WHERE id = :characterId")
    fun getCharacterDetails(characterId: Int): CharacterLocal

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCharacter(character: CharacterLocal)
}