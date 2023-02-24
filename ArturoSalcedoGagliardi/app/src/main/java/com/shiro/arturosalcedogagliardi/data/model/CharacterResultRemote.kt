package com.shiro.arturosalcedogagliardi.data.model

import com.shiro.arturosalcedogagliardi.data.model.character.CharacterRemote
import com.shiro.arturosalcedogagliardi.domain.model.Pager

class CharacterResultRemote(
    val info: Pager? = null,
    val results: List<CharacterRemote>? = null
)