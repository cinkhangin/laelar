package com.laelar.core

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.laelar.core.models.Unique
import com.naulian.anhance.failMessage
import com.naulian.anhance.success
import com.naulian.firex.storeCollection

object LicenseManager {
    @Suppress("unused")
    private val TAG = LicenseManager::class.java.simpleName

    private fun uniquesPath(appId: String) = storeCollection("apps").document(appId)
        .collection("uniques")

    private fun uniquePath(appId: String, deviceId: String) =
        uniquesPath(appId).document(deviceId)

    fun check(appId: String, deviceId: String, action: (Result<Unique>) -> Unit) {
        if (deviceId.isEmpty()) {
            action(failMessage("Error: Empty ID"))
            return
        }

        fetch(appId, deviceId) { result: Result<Unique> ->
            result.onFailure {
                fetch(AppIds.member, deviceId) { result2: Result<Unique> ->
                    result2.onFailure { action(result2) }
                    result2.onSuccess { login(it, action) }
                }
            }
            result.onSuccess { login(it, action) }
        }
    }

    private fun fetch(appId: String, deviceId: String, action: (Result<Unique>) -> Unit) {
        uniquePath(appId, deviceId).get().addOnSuccessListener {
            it.toObject<Unique>()?.apply {
                action(success(copy(deviceId = deviceId)))
            } ?: action(failMessage("Error: No Registration"))
        }.addOnFailureListener {
            action(failMessage("Error: Unable to connect"))
        }
    }

    private fun login(unique: Unique, action: (Result<Unique>) -> Unit) {
        Firebase.auth.signInAnonymously().addOnFailureListener {
            action(failMessage("Error: Unable to login"))
        }.addOnSuccessListener { action(success(unique)) }
    }
}