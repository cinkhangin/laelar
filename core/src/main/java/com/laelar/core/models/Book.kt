package com.laelar.core.models

import androidx.annotation.DrawableRes
import com.laelar.core.R
import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val id: String = "",

    val path: String = "",

    @DrawableRes
    val image: Int = R.drawable.placeholder,

    val name: String = "",
    val description: String = "",

    val chapters: List<Chapter> = emptyList(),
    val new: Boolean = true,
    val changed: Boolean = false,
)
