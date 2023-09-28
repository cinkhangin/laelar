package com.laelar.core.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blocks")
data class BlockData(
    @PrimaryKey
    val id: String = "",
    val version: Int = 0,
)
