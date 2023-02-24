package com.shiro.arturosalcedogagliardi.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shiro.arturosalcedogagliardi.data.model.character.CharacterLocal
import com.shiro.arturosalcedogagliardi.data.model.character.location.LocationLocal
import com.shiro.arturosalcedogagliardi.data.model.character.origin.OriginLocal

@Database(
    entities = [CharacterLocal::class, OriginLocal::class, LocationLocal::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun charactersDao(): CharactersDao
}