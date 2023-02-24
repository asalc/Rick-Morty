package com.shiro.arturosalcedogagliardi.data.model.character.location

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class LocationLocal(

    @PrimaryKey
    @ColumnInfo(name = "character_location_name")
    var locationName: String = "",

    @ColumnInfo(name = "character_location_url")
    var locationUrl: String? = null
)