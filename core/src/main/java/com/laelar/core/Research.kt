package com.laelar.core

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.fragment.app.Fragment
import com.laelar.core.models.Block
import com.naulian.anhance.html
import com.naulian.anhance.showToast
import com.naulian.anhance.uniqueSeed
import com.naulian.glow.CodeTheme
import com.naulian.glow.HighLight
import com.naulian.glow.Theme
import com.naulian.glow.glowSyntax

fun randomEmoji(): String {
    val pool = "😀 😁 😂 🤣 😃 😄 😅 😆 😉 😊 😋 😍 😘 🥰 😗 😙 😚 ☺️ 🙂 🤗 🤩 😛 😜 😝 🤤"
    return pool.split(" ").random(uniqueSeed)
}

fun Block.glow(theme: Theme = CodeTheme.kotlinLight): HighLight {
    return if (code.isEmpty()) HighLight("hello".html(), "hello")
    else glowSyntax(code, language, theme)
}

val Context.deviceId: String
    @SuppressLint("HardwareIds")
    get() = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

fun Fragment.openTelegram() {
    val telegram = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/+hPkaUSqO5HczZjA1"))
    telegram.setPackage("org.telegram.messenger")
    try {
        startActivity(telegram)
    } catch (e: ActivityNotFoundException) {
        showToast("Telegram is not installed")
    }
}

fun Fragment.openMessenger(pageId: String) {
    try {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("fb-messenger://user/$pageId"))
        startActivity(intent)
    } catch (e: Exception) {
        // Facebook Messenger app is not installed, open Messenger in a web browser
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.facebook.com/messages/t/$pageId")
        )
        startActivity(intent)
    }
}