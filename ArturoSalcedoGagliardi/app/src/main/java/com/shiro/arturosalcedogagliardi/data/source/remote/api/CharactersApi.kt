package com.shiro.arturosalcedogagliardi.data.source.remote.api

import com.shiro.arturosalcedogagliardi.data.model.CharacterResultRemote
import com.shiro.arturosalcedogagliardi.data.model.character.CharacterRemote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharactersApi {

    @GET("character/")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): Response<CharacterResultRemote>

    @GET("character/{characterId}")
    suspend fun getCharacterDetails(
        @Path("characterId") characterId: Int
    ): Response<CharacterRemote>

}