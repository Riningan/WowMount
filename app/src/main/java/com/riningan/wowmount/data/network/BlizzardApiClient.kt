package com.riningan.wowmount.data.network

import com.riningan.wowmount.data.network.api.BlizzardApi
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class BlizzardApiClient(okHttpClient: OkHttpClient) {
    private val mBlizzardApi: BlizzardApi


    init {
        mBlizzardApi = Retrofit.Builder()
                .baseUrl(EMPTY_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
                .create(BlizzardApi::class.java)
    }


    fun getClient() = mBlizzardApi
}