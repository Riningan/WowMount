package com.riningan.wowmount.data.network.api

import com.riningan.wowmount.data.network.model.AccessTokenResponse
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path


interface BattleNetApi {
    @POST("https://{region}.battle.net/oauth/token")
    @FormUrlEncoded
    fun getAccessToken(@Path("region") region: String, @FieldMap fields: Map<String, String>): Call<AccessTokenResponse>
}