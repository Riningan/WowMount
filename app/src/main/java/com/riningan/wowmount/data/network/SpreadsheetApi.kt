package com.riningan.wowmount.data.network

import com.riningan.wowmount.data.network.model.SpreadsheetCSVResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path


interface SpreadsheetApi {
    @GET("spreadsheets/d/e/2PACX-1vQG6zOwKcINcyMg0cQ_r3DncWtajvYqeifIWx5qOPu9aOaNyeGXNrIUNDgZIUA9kyriMRjBKc74WfIk/pub")
    fun getSpreadsheetRows(@Path("gid") gid: Int = 1929561215,
                           @Path("single") single: Boolean = true,
                           @Path("output") output: String = "csv"): Single<List<SpreadsheetCSVResponse>>
}