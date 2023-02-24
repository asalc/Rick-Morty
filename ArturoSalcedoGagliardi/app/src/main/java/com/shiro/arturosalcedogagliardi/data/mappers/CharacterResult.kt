package com.shiro.arturosalcedogagliardi.data.mappers

import com.shiro.arturosalcedogagliardi.data.model.CharacterResultRemote
import com.shiro.arturosalcedogagliardi.domain.model.CharacterResult

fun CharacterResultRemote.toDomain() =
    CharacterResult(
        info = this.info,
        results = results?.map { it.toDomain() }
    )