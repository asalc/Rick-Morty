package com.shiro.arturosalcedogagliardi.data.model.character.origin

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class OriginLocal(

    @PrimaryKey
    @ColumnInfo(name = "character_origin_name")
    var originName: String = "",

    @ColumnInfo(name = "character_origin_url")
    var originUrl: String? = null
)