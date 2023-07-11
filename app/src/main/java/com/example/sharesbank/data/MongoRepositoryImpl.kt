package com.example.sharesbank.data

import com.example.sharesbank.model.Portfolio
import com.example.sharesbank.model.Share
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MongoRepositoryImpl(val realm: Realm) : MongoRepository {
    override fun getPortfolios(): Flow<List<Portfolio>> {
        return realm.query<Portfolio>().asFlow().map { it.list }
    }

    override suspend fun insertShare(share: Share, portfolio: Portfolio) {
        realm.write {
            val query = query<Portfolio>(query = "name == $0", portfolio.name).first().find()
            if (query != null) {
                query.shares.add(share)
            }
        }
    }

    override suspend fun insertPortfolio(portfolio: Portfolio) {
        realm.write { copyToRealm(portfolio) }
    }

    override suspend fun deletePortfolio(id: String) {
        realm.write {
            val person = query<Portfolio>(query = "name == $0", id).first().find()
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
}