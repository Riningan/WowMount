package com.riningan.wowmount.data.network

import com.riningan.wowmount.data.network.model.SpreadsheetCSVResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface SpreadsheetApi {
    @GET("spreadsheets/d/e/2PACX-1vQG6zOwKcINcyMg0cQ_r3DncWtajvYqeifIWx5qOPu9aOaNyeGXNrIUNDgZIUA9kyriMRjBKc74WfIk/pub")
    fun getSpreadsheetRows(@Query("gid") gid: Int = 1929561215,
                           @Query("single") single: Boolean = true,
                           @Query("output") output: String = "csv"): Single<List<SpreadsheetCSVResponse>>
}