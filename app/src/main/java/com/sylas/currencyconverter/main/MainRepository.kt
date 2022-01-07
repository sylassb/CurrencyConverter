package com.sylas.currencyconverter.main

import com.sylas.currencyconverter.data.models.CurrencyResponse
import com.sylas.currencyconverter.util.Resource

interface MainRepository {

    suspend fun getRates(base: String): Resource<CurrencyResponse>
}