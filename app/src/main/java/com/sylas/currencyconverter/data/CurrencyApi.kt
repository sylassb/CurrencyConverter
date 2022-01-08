package com.sylas.currencyconverter.data

import com.sylas.currencyconverter.data.models.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET("/latest?access_key=ba8c897ecf3648d66f5afb720329d66b")
    suspend fun getRates(
        @Query("base") base: String
    ): Response<CurrencyResponse>
}