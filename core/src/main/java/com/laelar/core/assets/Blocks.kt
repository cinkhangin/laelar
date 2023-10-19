package com.laelar.core.assets

import android.content.Context
import com.laelar.core.glow
import com.laelar.core.models.Block
import com.naulian.anhance.readStringAsset
import com.naulian.glow.CodeTheme
import kotlinx.serialization.json.Json

object Blocks {
    @Suppress("unused")
    private val TAG = Blocks::class.java.simpleName

    fun list(chapterId: String) = Resources.getBlocks(chapterId)

    fun load(context: Context, filename: String, injectionMap : Map<String, Block>): List<Block> {
        val codeTheme = CodeTheme.default(context)
        val source = context.readStringAsset(filename)
        val list = Json.decodeFromString<List<Block>>(source)
        return list.map {
            val block = injectionMap[it.injectionId] ?: it
            block.copy(codeSpanned = block.glow(codeTheme).spanned)
        }
    }
}