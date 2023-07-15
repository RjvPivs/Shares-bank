package com.example.sharesbank.data

import com.example.sharesbank.adapter.Web
import com.example.sharesbank.model.Portfolio
import com.example.sharesbank.model.Share
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.mongodb.kbson.ObjectId

class MongoRepositoryImpl(val realm: Realm) : MongoRepository {
    override fun getPortfolios(): Flow<List<Portfolio>> {
        return realm.query<Portfolio>().asFlow().map { it.list }
    }

    override suspend fun getShare(share: Share): Share? {
        return realm.query<Share>(query = "name = $0", share.name).first().find()
    }

    override suspend fun insertShare(share: Share, portfolio: Portfolio) {
        realm.write {
            val query = query<Portfolio>(query = "name == $0", portfolio.name).first().find()
            query?.shares?.add(share)
        }
    }

    override suspend fun deleteShare(share: Share, portfolio: Portfolio) {
        realm.write {
            val query = query<Portfolio>(query = "name == $0", portfolio.name).first().find()
            val query1 = query<Share>(query = "name == $0", share.name).first().find()
            query?.shares?.remove(share)
            try {
                if (query1 != null) {
                    delete(query1)
                }
            } catch (_: Exception) {

            }
        }
    }

    override suspend fun insertPortfolio(portfolio: Portfolio) {
        realm.write { copyToRealm(portfolio) }
    }

    override suspend fun deletePortfolio(id: ObjectId) {
        realm.write {
            val person = query<Portfolio>(query = "_id == $0", id).first().find()
            try {
                person?.let { delete(it) }
            } catch (_: Exception) {

            }
        }
    }

    override suspend fun getPortfolio(name: String): Portfolio? {
        return realm.query<Portfolio>(query = "name = $0", name).first().find()
    }

    override suspend fun updatePortfolio(portfolio: Portfolio) {
        realm.write {
            val query = query<Portfolio>(query = "name == $0", portfolio.name).first().find()
            if (query != null) {
                query.shares = portfolio.shares
            }
        }
    }

    override suspend fun updateShare(share: Share, portfolio: Portfolio) {
        realm.write {
            val query = query<Portfolio>(query = "_id == $0", portfolio._id).first().find()
            if (query != null) {
                query.shares.find { it.name == share.name }!!.number = share.number
                query.shares.find { it.name == share.name }!!.totalCost = share.totalCost
                query.shares.find { it.name == share.name }!!.actualPrice = share.actualPrice
            }
            val query1 = query<Share>(query = "name == $0", share.name).first().find()
            query1!!.number = share.number
            query1.totalCost = share.totalCost
            query1.actualPrice = share.actualPrice
        }
    }

    override suspend fun updatePortfolioName(name: String, newName: String) {
        realm.write {
            val query = query<Portfolio>(query = "name == $0", name).first().find()
            if (query != null) {
                query.name = newName
            }
        }
    }

    override suspend fun updateSharesPrices(portfolio: Portfolio): Double {
        var profit = 0.0
        realm.write {
            val query = query<Portfolio>(query = "name == $0", portfolio.name).first().find()
            query?.shares?.forEach {
                var temp = 0.0
                temp = Web.getSharePrice(it.name)
                it.actualPrice = temp
                val query1 = query<Share>(query = "name == $0", it.name).first().find()
                query1?.actualPrice = temp
                profit += it.getProfit()
            }
        }
        return profit
    }
}