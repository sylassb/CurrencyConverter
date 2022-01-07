package com.sylas.currencyconverter.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sylas.currencyconverter.data.models.Rates
import com.sylas.currencyconverter.util.DispatcherProvider
import com.sylas.currencyconverter.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val repository: MainRepository,
    private val dispatchers: DispatcherProvider
): ViewModel() {

    sealed class CurrencyEvent {
        class Success(val resultText: String): CurrencyEvent()
        class Failure(val errorText: String): CurrencyEvent()
        object Loading : CurrencyEvent()
        object Empty : CurrencyEvent()
    }

    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion: StateFlow<CurrencyEvent> = _conversion

    fun convert(
        amountStr: String,
        fromCurrency: String,
        toCurrency: String
    ) {
        val fromAmount = amountStr.toFloatOrNull()
        if(fromAmount == null) {
            _conversion.value = CurrencyEvent.Failure("Not a valid amount")
            return
        }

        viewModelScope.launch(dispatchers.io) {
            _conversion.value = CurrencyEvent.Loading
            when(val ratesResponse = repository.getRates(fromCurrency)) {
                is Resource.Error -> _conversion.value = CurrencyEvent.Failure(ratesResponse.message!!)
                is Resource.Success -> {
                    val rates = ratesResponse.data!!.rates
                    val rate = getRateForCurrency(toCurrency, rates)
                    if(rate == null) {
                        _conversion.value = CurrencyEvent.Failure("Unexpected error")
                    } else {
                        val convertedCurrency = kotlin.math.round(fromAmount * rate * 100) / 100
                        _conversion.value = CurrencyEvent.Success(
                            "$fromAmount $fromCurrency = $convertedCurrency $toCurrency"
                        )
                    }
                }
            }
        }
    }

    private fun getRateForCurrency(currency: String, rates: Rates) = when (currency) {
            "AED" -> rates.AED
            "AFN" -> rates.AFN
            "ALL" -> rates.ALL
            "AMD" -> rates.AMD
            "ANG" -> rates.ANG
            "AOA" -> rates.AOA
            "ARS" -> rates.ARS
            "AUD" -> rates.AUD
            "AWG" -> rates.AWG
            "AZN" -> rates.AZN
            "BAM" -> rates.BAM
            "BBD" -> rates.BBD
            "BDT" -> rates.BDT
            "BGN" -> rates.BGN
            "BHD" -> rates.BHD
            "BIF" -> rates.BIF
            "BMD" -> rates.BMD
            "BND" -> rates.BND
            "BOB" -> rates.BOB
            "BRL" -> rates.BRL
            "BSD" -> rates.BSD
            "BTC" -> rates.BTC
            "BTN" -> rates.BTN
            "BWP" -> rates.BWP
            "BYN" -> rates.BYN
            "BYR" -> rates.BYR
            "BZD" -> rates.BZD
            "CAD" -> rates.CAD
            "CDF" -> rates.CDF
            "CHF" -> rates.CHF
            "CLF" -> rates.CLF
            "CLP" -> rates.CLP
            "CNY" -> rates.CNY
            "COP" -> rates.COP
            "CRC" -> rates.CRC
            "CUC" -> rates.CUC
            "CUP" -> rates.CUP
            "CVE" -> rates.CVE
            "CZK" -> rates.CZK
            "DJF" -> rates.DJF
            "DKK" -> rates.DKK
            "DOP" -> rates.DOP
            "DZD" -> rates.DZD
            "EGP" -> rates.EGP
            "ERN" -> rates.ERN
            "ETB" -> rates.ETB
            "EUR" -> rates.EUR
            "FJD" -> rates.FJD
            "FKP" -> rates.FKP
            "GBP" -> rates.GBP
            "JPY" -> rates.JPY
            "KHR" -> rates.KHR
            "NZD" -> rates.NZD
            "RSD" -> rates.RSD
            "RUB" -> rates.RUB
            "USD" -> rates.USD
            "ZAR" -> rates.ZAR
        else -> null
    }
}