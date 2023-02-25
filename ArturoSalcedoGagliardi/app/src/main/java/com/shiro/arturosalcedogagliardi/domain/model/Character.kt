package com.shiro.arturosalcedogagliardi.domain.model

import java.io.Serializable

class Character(
    val id: Int? = null,
    var name: String? = null,
    val status: String? = null,
    var species: String? = null,
    val gender: String? = null,
    var origin: String? = null,
    var location: String? = null,
    val image: String? = null
): Serializable