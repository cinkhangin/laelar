package com.laelar.core.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp

data class Unique(
    @get:Exclude
    val deviceId: String = "",

    @ServerTimestamp
    val created: Timestamp? = null,
) {
    @Suppress("unused")
    companion object {
        const val DEVICE_ID = "deviceId"
        const val CREATED = "created"
        val new get() = Unique()
    }
}
