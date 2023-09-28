package com.laelar.core.assets

import android.content.Context
import com.laelar.core.database.AppDatabase
import com.laelar.core.models.Block
import com.laelar.core.models.BlockData
import com.laelar.core.models.Book
import com.laelar.core.models.Chapter
import com.naulian.anhance.observe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine

object Resources {
    @Suppress("unused")
    private val TAG = Resources::class.java.simpleName

    private var bookList = listOf<Book>()
    private val mutableBooks = MutableStateFlow(listOf<Book>())
    val books = mutableBooks.asStateFlow()

    private val chaptersMap = hashMapOf<String, List<Chapter>>()
    private val chapterMap = hashMapOf<String, Chapter>()

    private val blockListMap = hashMapOf<String, List<Block>>()

    private var loaded = false
    val shouldPostPone get() = !loaded

    fun CoroutineScope.load(context: Context, injectMap: Map<String, Block>) {
        val database = AppDatabase.create(context)
        val dao = database.dao()

        bookList = Books.load(context)

        val newBookList = mutableListOf<Book>()
        bookList.forEach { book ->
            val chapterList = Chapters.load(context, book.path)
            chaptersMap[book.id] = chapterList

            chapterList.forEach { chapter ->
                val blockList = Blocks.load(context, chapter.path, injectMap)
                blockListMap[chapter.id] = blockList
                chapterMap[chapter.id] = chapter
            }

            val newBook = book.copy(chapters = chapterList)
            newBookList.add(newBook)
        }

        bookList = newBookList.toList()
        loaded = true

        val flowData = dao.chaptersFlow()
            .combine(dao.blocksFlow()) { c, b -> Pair(c, b) }

        observe(flowData) {
            processData(it.first, it.second)
        }
    }

    private fun processData(chapterData: List<Chapter>, blockData: List<BlockData>) {
        val blockMap = blockData.associateBy { it.id }
        val progressMap = chapterData.associateBy { it.id }
        val newBookList = arrayListOf<Book>()

        bookList.forEach { book ->
            val newChapterList = mutableListOf<Chapter>()
            book.chapters.forEach { chapter ->

                val blocks = blockListMap[chapter.id]?.map {
                    val version = blockMap[it.id]?.version ?: 0
                    val isChanged = it.version != 0 && it.version > version
                    it.copy(changed = isChanged)
                } ?: emptyList()

                val newChapter = chapter.copy(
                    learned = progressMap[chapter.id]?.learned ?: false,
                    new = !progressMap.containsKey(chapter.id),
                    changed = blocks.any { it.changed }
                )

                blockListMap[chapter.id] = blocks
                newChapter.blocks = blocks
                newChapterList.add(newChapter)
            }

            val newBook = book.copy(
                chapters = newChapterList,
                new = newChapterList.any { it.new },
                changed = newChapterList.any { it.changed }
            )
            newBookList.add(newBook)
            chaptersMap[book.id] = newChapterList
        }

        bookList = newBookList.toList()
        mutableBooks.value = bookList
    }

    fun getChapters(bookId: String) = chaptersMap[bookId] ?: emptyList()

    //fun getChapter(chapterId: String) = chapterMap[chapterId] ?: Chapter(chapterId)
    fun getBlocks(chapterId: String) = blockListMap[chapterId] ?: emptyList()
}