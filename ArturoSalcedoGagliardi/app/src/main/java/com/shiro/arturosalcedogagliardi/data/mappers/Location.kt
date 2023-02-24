package com.shiro.arturosalcedogagliardi.data.mappers

import com.shiro.arturosalcedogagliardi.data.model.character.location.LocationLocal
import com.shiro.arturosalcedogagliardi.data.model.character.location.LocationRemote

fun LocationRemote.toLocal() =
    LocationLocal(
        locationName = this.name ?: "",
        locationUrl = this.url
    )

fun LocationRemote.toDomain() = this.name ?: ""
fun LocationLocal.toDomain() = this.locationName