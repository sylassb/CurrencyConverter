package com.sylas.currencyconverter.data.models

import com.google.gson.annotations.SerializedName

data class CurrencyResponse(
    @SerializedName("base")
    val base: String,
    val date: String,
    val rates: Rates
)