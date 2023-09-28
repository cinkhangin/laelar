package com.laelar.core.database

import android.content.Context
import com.laelar.core.assets.Blocks
import com.laelar.core.assets.Books
import com.laelar.core.assets.Chapters
import com.laelar.core.models.Block
import com.laelar.core.models.BlockData
import com.laelar.core.models.Chapter
import com.naulian.anhance.observe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object DataManager {
    private val mutableChapter = MutableStateFlow(Chapter())
    val chapter = mutableChapter.asStateFlow()

    fun clearChapter() {
        mutableChapter.value = Chapter()
    }

    fun CoroutineScope.loadChapter(dao: Dao, chapterId: String) {
        val blocks = Blocks.list(chapterId)
        observe(dao.chapterFlow(chapterId)) {
            val chapter = it ?: Chapter(chapterId)
            chapter.blocks = blocks
            mutableChapter.value = chapter
        }
    }

    suspend fun cleanUp(context: Context, onComplete: () -> Unit) {
        val database = AppDatabase.create(context)
        val dao = database.dao()

        val books = Books.load(context)

        val progress = dao.getChapters()
        val chapters = mutableListOf<Chapter>()

        for (book in books) {
            val contents = Chapters.load(context, book.path)
            chapters.addAll(contents)
        }

        val chapterMap = chapters.associateBy { it.id }
        for (item in progress) {
            chapterMap[item.id] ?: dao.delete(item)
        }

        onComplete()
    }

    suspend fun learn(dao: Dao, chapterId: String) {
        val chapter = Chapter(
            id = chapterId,
            learned = true,
        )
        dao.insert(chapter)
    }

    suspend fun unLearned(dao: Dao, chapterId: String) {
        val chapter = Chapter(
            id = chapterId,
            learned = false,
        )
        dao.update(chapter)
    }

    suspend fun view(dao: Dao, chapterId: String) {
        val chapter = Chapter(
            id = chapterId,
            learned = false
        )
        dao.getChapter(chapterId)?.let {
            return
        } ?: dao.insert(chapter)
    }

    suspend fun viewBlocks(dao: Dao, list: List<Block>) {
        list.filter { it.changed }.forEach {
            val blockData = BlockData(id = it.id, version = it.version)
            dao.insert(blockData)
        }
    }
}