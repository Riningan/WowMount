package com.riningan.wowmount.data.network

import com.riningan.retrofit2.converter.csv.CsvConverterFactory
import com.riningan.wowmount.data.network.api.SpreadsheetApi
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit


class SpreadSheetApiClient {
    private val mSpreadsheetApi: SpreadsheetApi


    init {
        val okHttpClient = OkHttpClient().newBuilder()
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build()

        mSpreadsheetApi = Retrofit.Builder()
                .baseUrl(DOCS_URL)
                .client(okHttpClient)
                .addConverterFactory(CsvConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
                .create(SpreadsheetApi::class.java)
    }


    fun getClient() = mSpreadsheetApi


    companion object {
        private const val DOCS_URL = "https://docs.google.com"
    }
}