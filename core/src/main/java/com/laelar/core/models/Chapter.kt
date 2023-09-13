package com.laelar.core.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "chapters")
@Serializable
data class Chapter(
    @PrimaryKey
    val id: String = "",

    val bookId: String = "",
    val path: String = "",
    val title: String = "",
    val description: String = "",

    val learned: Boolean = false,
    val new: Boolean = true,
) {
    @Ignore
    var blocks: List<Block> = emptyList()
}
