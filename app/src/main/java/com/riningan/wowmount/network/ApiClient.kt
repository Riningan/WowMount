package com.riningan.wowmount.network

import com.google.gson.GsonBuilder
import com.riningan.wowmount.BuildConfig
import com.riningan.wowmount.data.LocalPreferences
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit
import io.reactivex.schedulers.Schedulers
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class ApiClient constructor(private val mLocalPreferences: LocalPreferences) {
    private var mBlizzardApi: BlizzardApi
    private var mBattleNetApi: BattleNetApi

    init {
        val authHttpClient = OkHttpClient().newBuilder()
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor { chain ->
                    val original = chain.request()
                    val requestBuilder = original.newBuilder()
                            .header("Authorization", Credentials.basic(CLIENT_ID, BuildConfig.CLIENT_SECRET))
                            .header("Content-Type", "application/json")
                            .method(original.method(), original.body())
                    val request = requestBuilder.build()
                    chain.proceed(request)
                }
                .build()
        mBattleNetApi = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(authHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BattleNetApi::class.java)

        val blizzardHttpClient = OkHttpClient().newBuilder()
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor { chain ->
                    val original = chain.request()
                    if (mLocalPreferences.getAccessToken() == null) {
                        mLocalPreferences.setAccessToken(requestAccessToken(original))
                    }
                    try {
                        chain.proceed(createNewRequest(original))
                    } catch (e: Exception) {
                        // request new access token
                        mLocalPreferences.setAccessToken(requestAccessToken(original))
                        chain.proceed(createNewRequest(original))
                    }
                }
                .build()
        mBlizzardApi = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(blizzardHttpClient)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
                .create(BlizzardApi::class.java)
    }


    fun getBlizzardApi() = mBlizzardApi


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
                .header("Authorization", "Bearer " + mLocalPreferences.getAccessToken())
                .header("Content-Type", "application/json")
                .method(original.method(), original.body())
        return requestBuilder.build()
    }


    companion object {
        private const val TIMEOUT = 30000L
        private const val CLIENT_ID = "9dbd799cb01a4ce194705b6cbf652875"
        private const val BASE_URL = "http://worldofwarcraft.com"
        private val REGEX = "(\\w\\w)\\.api\\.blizzard\\.com".toRegex()
    }
}