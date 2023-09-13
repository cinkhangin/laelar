package com.laelar.core.assets

import android.content.Context
import com.laelar.core.models.Book
import com.naulian.anhance.readStringAsset
import kotlinx.serialization.json.Json

object Books {
    fun list() = Resources.books

    fun load(context: Context): List<Book> {
        val source = context.readStringAsset("books.json")
        return Json.decodeFromString(source)
    }
}