package com.example.sharesbank.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Share : RealmObject {
    @Index
    @PrimaryKey
    var name: String = ""
    var price: Double = 0.0
}