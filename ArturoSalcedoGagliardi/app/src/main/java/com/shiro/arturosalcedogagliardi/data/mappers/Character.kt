package com.shiro.arturosalcedogagliardi.data.mappers

import com.shiro.arturosalcedogagliardi.data.model.character.CharacterLocal
import com.shiro.arturosalcedogagliardi.data.model.character.CharacterRemote
import com.shiro.arturosalcedogagliardi.domain.model.Character
import com.shiro.arturosalcedogagliardi.domain.model.CharacterResult

fun CharacterRemote.toLocal() =
    CharacterLocal(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        type = this.type,
        gender = this.gender,
        origin = this.origin?.toLocal(),
        location = this.location?.toLocal(),
        image = this.image,
        url = this.url,
        created = this.created
    )

fun CharacterRemote.toDomain() =
    Character(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        gender = this.gender,
        origin = this.origin?.toDomain(),
        location = this.location?.toDomain(),
        image = this.image
    )

fun CharacterLocal.toDomain() =
    Character(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        gender = this.gender,
        origin = this.origin?.toDomain(),
        location = this.location?.toDomain(),
        image = this.image
    )

fun List<CharacterLocal>.toDomain() = this.map { it.toDomain() }
fun List<CharacterLocal>.toCharacterResult() =
    CharacterResult(
        null,
        this.toDomain()
    )