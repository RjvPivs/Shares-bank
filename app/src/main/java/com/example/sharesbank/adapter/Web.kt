package com.example.sharesbank.adapter

import com.crazzyghost.alphavantage.AlphaVantage
import com.crazzyghost.alphavantage.Config
import com.crazzyghost.alphavantage.timeseries.response.QuoteResponse
import com.example.sharesbank.data.DatabaseModule
import com.example.sharesbank.model.Portfolio
import com.example.sharesbank.model.Share
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
            return response.price
        }

        fun getSharePriceAsync(share: Share, portfolio: Portfolio) {
            AlphaVantage.api().init(cfg);
            AlphaVantage.api()
                .timeSeries()
                .quote()
                .forSymbol(share.name).onSuccess { e -> update(e, share, portfolio) }
                .fetch()
        }

        private fun update(e: Any, share: Share, portfolio: Portfolio) {
            var response: QuoteResponse = e as QuoteResponse
            var shareNew = Share()
            shareNew.name = share.name
            shareNew.number = share.number
            shareNew.totalCost = share.totalCost
            shareNew.actualPrice = response.price
            runBlocking {
                launch {
                    DatabaseModule.provideMongoRepository(DatabaseModule.provideRealm())
                        .updateShare(shareNew, portfolio)
                }
            }
        }
    }
}
