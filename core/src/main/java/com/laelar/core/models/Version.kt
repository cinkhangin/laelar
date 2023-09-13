package com.laelar.core.models

import kotlinx.serialization.Serializable

@Serializable
data class Version(
    val code: Int = 0,
    val name: String = ""
)
