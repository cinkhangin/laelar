package com.laelar.core.models

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.text.Spanned
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.laelar.core.R
import com.laelar.core.assets.Icons
import com.naulian.anhance.html
import com.naulian.anhance.toUri
import kotlinx.serialization.Serializable

@Serializable
data class Block(
    val id: Int = 0,

    val header: String = "",
    val centered: Boolean = false,
    val title: String = "",
    val label: String = "",

    @DrawableRes
    val drawable: Int = 0,
    val image: String = "",
    val description: String = "",

    val subTitle: String = "",
    val subHeader: String = "",
    val body: String = "",
    val note: String = "",

    val quote: String = "",
    val author: String = "Undefined",

    @DrawableRes
    val icon: Int = Icons.link,
    val link: String = "",
    val hyper: String = "",
    val onLink: ((String) -> Unit)? = null,

    val code: String = "",
    val codeSpanned: Spanned = "".html(),
    @DrawableRes
    val copyIcon: Int = Icons.copy,
    val language: String = "",
    val copyable: Boolean = false,
    val selectable: Boolean = true,
    val onCopy: ((Block) -> Unit)? = null,
    val extra: String = "",

    @ColorRes
    val bgColor: Int = 0,
    val bgHex: String = "",

    val hint: String = "",
    val helper: String = "",
    val input: String = "",

    val button: String = "",
    val enable: Boolean = true,
    val onClick: ((Block) -> Unit)? = null,
    val output: String = "",

    val injectionId: String = "",
) {
    val quoteText
        get() = """
        |$quote
        |-$author
    """.trimMargin()

    fun imageUri(context: Context): Uri {
        return if (image.isNotEmpty()) Uri.parse(image)
        else if (drawable != 0) drawable.toUri(context)
        else R.drawable.placeholder.toUri(context)
    }

    fun background(context: Context): Int {
        return if (bgHex.isNotEmpty()) Color.parseColor(bgHex)
        else if (bgColor != 0) context.getColor(bgColor)
        else context.getColor(R.color.background)
    }
}
