package com.example.sharesbank.data

import com.example.sharesbank.model.Portfolio
import com.example.sharesbank.model.Share
import kotlinx.coroutines.flow.Flow

interface MongoRepository {
    fun getPortfolios(): Flow<List<Portfolio>>

    suspend fun getShare(share: Share) : Share?
    suspend fun insertShare(share: Share, portfolio: Portfolio)
    suspend fun insertPortfolio(portfolio: Portfolio)
    suspend fun deletePortfolio(name: String)
    suspend fun getPortfolio(name: String): Portfolio?
    suspend fun deleteShare(share: Share, portfolio: Portfolio)
    suspend fun updatePortfolio(portfolio: Portfolio)

    suspend fun updateShare(share: Share, portfolio: Portfolio)
}