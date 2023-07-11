package com.example.sharesbank.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Portfolio() : RealmObject{
    @Index
    @PrimaryKey
    var name: String = ""
    var approximate: Double = 0.0
    var number: Int = 0
    var shares: RealmList<Share> = realmListOf()

}
