package com.riningan.wowmount.data.network

import com.riningan.retrofit2.converter.csv.CsvConverterFactory
import com.riningan.wowmount.BuildConfig
import com.riningan.wowmount.data.network.api.BattleNetApi
import com.riningan.wowmount.data.network.api.BlizzardApi
import com.riningan.wowmount.data.network.api.SpreadsheetApi
import com.riningan.wowmount.data.preferences.LocalPreferences
import io.reactivex.schedulers.Schedulers
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


class ApiClient constructor(private val mLocalPreferences: LocalPreferences) {
    private val mBlizzardApi: BlizzardApi
    private val mBattleNetApi: BattleNetApi
    private val mSpreadsheetApi: SpreadsheetApi


    init {
        val authHttpClient = OkHttpClient().newBuilder()
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor { chain ->
                    val original = chain.request()
                    val requestBuilder = original.newBuilder()
                            .header("Authorization", Credentials.basic(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET))
                            .header("Content-Type", "application/json")
                            .method(original.method(), original.body())
                    val request = requestBuilder.build()
                    chain.proceed(request)
                }
                .build()
        mBattleNetApi = Retrofit.Builder()
                .baseUrl(EMPTY_URL)
                .client(authHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BattleNetApi::class.java)

        val blizzardHttpClient = OkHttpClient().newBuilder()
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor { chain ->
                    val original = chain.request()
                    if (mLocalPreferences.accessToken.isEmpty()) {
                        mLocalPreferences.accessToken = requestAccessToken(original)
                    }
                    var response = chain.proceed(createNewRequest(original))
                    when (response.code()) {
                        401 -> {
                            // request new access token
                            mLocalPreferences.accessToken = requestAccessToken(original)
                            response = chain.proceed(createNewRequest(original))
                        }
                    }
                    response
                }
                .build()
        mBlizzardApi = Retrofit.Builder()
                .baseUrl(EMPTY_URL)
                .client(blizzardHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
                .create(BlizzardApi::class.java)

        val spreadsheetHttpClient = OkHttpClient().newBuilder()
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build()
        mSpreadsheetApi = Retrofit.Builder()
                .baseUrl(DOCS_URL)
                .client(spreadsheetHttpClient)
                .addConverterFactory(CsvConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
                .create(SpreadsheetApi::class.java)
    }


    fun getBlizzardApi() = mBlizzardApi

    fun getSpreadsheetApi() = mSpreadsheetApi


    private fun requestAccessToken(original: Request): String {
        val host = original.url().host()!!
        return REGEX.find(host)?.let { matchResult ->
            val params = HashMap<String, String>()
            params["grant_type"] = "client_credentials"
            mBattleNetApi.getAccessToken(matchResult.groupValues[1], params).execute()?.let { response ->
                if (response.isSuccessful) {
                    response.body()?.accessToken ?: throw IOException("Null body")
                } else {
                    throw IOException("Unsuccess request")
                }
            } ?: throw IOException("Null response")
        } ?: throw IOException("Null server name")
    }

    private fun createNewRequest(original: Request): Request {
        val requestBuilder = original.newBuilder()
                .header("Authorization", "Bearer " + mLocalPreferences.accessToken)
                .header("Content-Type", "application/json")
                .method(original.method(), original.body())
        return requestBuilder.build()
    }


    companion object {
        private const val TIMEOUT = 30000L
        private const val EMPTY_URL = "http://this.is.not.valid.url"
        private const val DOCS_URL = "https://docs.google.com"
        private val REGEX = "(\\w\\w)\\.api\\.blizzard\\.com".toRegex()
    }
}