package com.example.sharesbank.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Share : RealmObject {
    @Index
    @PrimaryKey
    var name: String = ""
    var totalCost: Double = 0.0
    var number: Int = 0
    var actualPrice: Double = 0.0
    fun getAveragePrice(): Double {
        return totalCost / number
    }
    fun getProfit():Double{
        return actualPrice * number - totalCost
    }
}