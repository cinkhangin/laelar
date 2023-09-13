package com.laelar.core.assets

import android.content.Context
import com.laelar.core.models.Chapter
import com.naulian.anhance.readStringAsset
import kotlinx.serialization.json.Json

object Chapters {

    fun list(bookId: String) = Resources.getChapters(bookId)

    fun load(context: Context, filename : String): List<Chapter> {
        val source = context.readStringAsset(filename)
        return Json.decodeFromString(source)
    }
}