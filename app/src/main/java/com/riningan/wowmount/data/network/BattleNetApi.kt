package com.riningan.wowmount.data.network

import com.riningan.wowmount.data.network.model.AccessTokenResponse
import retrofit2.Call
import retrofit2.http.*


interface BattleNetApi {
    @POST("https://{region}.battle.net/oauth/token")
    @FormUrlEncoded
    fun getAccessToken(@Path("region") region: String, @FieldMap fields: Map<String, String>): Call<AccessTokenResponse>
}