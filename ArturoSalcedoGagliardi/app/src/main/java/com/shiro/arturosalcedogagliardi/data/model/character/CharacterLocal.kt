package com.shiro.arturosalcedogagliardi.data.model.character

import androidx.annotation.NonNull
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shiro.arturosalcedogagliardi.data.model.character.location.LocationLocal
import com.shiro.arturosalcedogagliardi.data.model.character.origin.OriginLocal

@Entity
class CharacterLocal(
    @PrimaryKey
    var id: Int? = null,

    var name: String? = null,
    var status: String? = null,
    var species: String? = null,
    var type: String? = null,
    var gender: String? = null,

    @Embedded
    var origin: OriginLocal? = null,

    @Embedded
    var location: LocationLocal? = null,

    var image: String? = null,
    var url: String? = null,
    var created: String? = null
)