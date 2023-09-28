package com.laelar.app

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.fragment.app.Fragment

@SuppressLint("HardwareIds")
fun Context.deviceId(): String =
    Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

fun Fragment.requireDeviceId(): String = requireContext().deviceId()