package com.riningan.wowmount.data.network.api

import com.riningan.wowmount.data.network.model.CharacterResponse
import com.riningan.wowmount.data.network.model.MountsResponse
import com.riningan.wowmount.utils.LocaleUtil
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface BlizzardApi {
    @GET("https://{region}.api.blizzard.com/wow/character/{realm}/{characterName}")
    fun getCharacter(@Path("region") region: String,
                     @Path("realm") realm: String,
                     @Path("characterName") characterName: String,
                     @Query("fields") fields: String = "mounts",
                     @Query("locale") locale: String = LocaleUtil.getLocale()): Single<CharacterResponse>

    @GET("https://{region}.api.blizzard.com/wow/mount/")
    fun getMounts(@Path("region") region: String,
                  @Query("locale") locale: String = LocaleUtil.getLocale()): Single<MountsResponse>
}