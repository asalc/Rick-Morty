package com.shiro.arturosalcedogagliardi.data.model.character

import com.shiro.arturosalcedogagliardi.data.model.character.location.LocationRemote
import com.shiro.arturosalcedogagliardi.data.model.character.origin.OriginRemote

class CharacterRemote(
    val id: Int? = null,
    val name: String? = null,
    val status: String? = null,
    val species: String? = null,
    val type: String? = null,
    val gender: String? = null,
    val origin: OriginRemote? = null,
    val location: LocationRemote? = null,
    val image: String? = null,
    val url: String? = null,
    val created: String? = null
)