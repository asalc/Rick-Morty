package com.shiro.arturosalcedogagliardi.data.mappers

import com.shiro.arturosalcedogagliardi.data.model.character.origin.OriginLocal
import com.shiro.arturosalcedogagliardi.data.model.character.origin.OriginRemote

fun OriginRemote.toLocal() =
    OriginLocal(
        originName = this.name ?: "",
        originUrl = this.url
    )

fun OriginRemote.toDomain() = this.name ?: ""
fun OriginLocal.toDomain() = this.originName