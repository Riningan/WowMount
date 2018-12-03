package com.riningan.wowmount.network

import com.riningan.wowmount.network.model.CharacterResponse
import com.riningan.wowmount.network.model.MountsResponse
import io.reactivex.Observable
import retrofit2.http.*


interface BlizzardApi {
    @GET("https://{region}.api.blizzard.com/wow/character/{realm}/{characterName}")
    fun getCharacter(@Path("region") region: String,
                  @Path("realm") realm: String,
                  @Path("characterName") characterName: String,
                  @Query("fields") fields: String,
                  @Query("locale") locale: String): Observable<CharacterResponse>

    @GET("https://{region}.api.blizzard.com/wow/mount/")
    fun getMounts(@Path("region") region: String,
                  @Query("locale") locale: String): Observable<MountsResponse>
}