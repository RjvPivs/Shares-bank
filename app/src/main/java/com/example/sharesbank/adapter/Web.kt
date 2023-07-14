package com.example.sharesbank.adapter

import com.crazzyghost.alphavantage.AlphaVantage
import com.crazzyghost.alphavantage.Config
import com.crazzyghost.alphavantage.exchangerate.ExchangeRateResponse
import com.crazzyghost.alphavantage.timeseries.response.QuoteResponse
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class Web {
    companion object Web {
        var cfg: Config = Config.builder()
            .key("NZ3V3EJEWUQAO5KD")
            .timeOut(10)
            .build()

        fun getSharePrice(share: String): Double {

            AlphaVantage.api().init(cfg);
            val response = AlphaVantage.api()
                .timeSeries()
                .quote()
                .forSymbol(share)
                .fetchSync()
            val rate =
                AlphaVantage.api().exchangeRate().fromCurrency("USD").toCurrency("RUB")
                    .fetchSync()
            return response.price * rate.exchangeRate
        }

    }
}
