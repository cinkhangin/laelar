package com.laelar.prime

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.laelar.core.AppIds
import com.laelar.core.models.Unique
import com.laelar.prime.databinding.ActivityMainBinding
import com.naulian.anhance.onClick
import com.naulian.anhance.showToast
import com.naulian.anhance.str
import com.naulian.anhance.textTrim
import com.naulian.anhance.toastError
import com.naulian.firex.nullableUid
import com.naulian.firex.signInWithEmailAndPassword
import com.naulian.firex.storeCollection

class MainActivity : AppCompatActivity() {
    @Suppress("unused")
    private val TAG = MainActivity::class.java.simpleName
    private lateinit var binding: ActivityMainBinding

    private fun appsPath(appId: String) =
        storeCollection("apps").document(appId)
            .collection("uniques")

    private fun uniquePath(appId: String, deviceId: String) =
        appsPath(appId).document(deviceId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (nullableUid == null) {
            binding.loadSrceen.show()
            val email = "admin@laelar.com"
            val password = "password"

            signInWithEmailAndPassword(email, password) { result ->
                result.onSuccess {
                    binding.loadSrceen.hide()
                }
                result.onFailure { toastError(it) }
            }
        }

        binding.apply {
            val items = AppIds.array.sortedArray()
            (textField as? MaterialAutoCompleteTextView)?.setSimpleItems(items)

            buttonRegister.onClick {
                val deviceId = binding.editDeviceId.textTrim()

                val appId = textField.textTrim()
                if (appId.isEmpty() || deviceId.isEmpty()) {
                    showToast("error")
                    return@onClick
                }

                uniquePath(appId, deviceId).set(Unique.new).addOnSuccessListener {
                    binding.editDeviceId.text?.clear()
                    showToast("registered!")
                    sync(appId)
                }.addOnFailureListener { toastError(it) }
            }
        }
    }

    private fun sync(appId: String) {
        appsPath(appId).get().addOnSuccessListener {
            binding.textCount.text = str("${it.documents.size} Users")
        }
    }
}