package com.laelar.core.assets

import android.content.Context
import com.laelar.core.database.AppDatabase
import com.laelar.core.models.Block
import com.laelar.core.models.Book
import com.laelar.core.models.Chapter
import com.naulian.anhance.observe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object Resources {
    @Suppress("unused")
    private val TAG = Resources::class.java.simpleName

    private var bookList = listOf<Book>()
    private val mutableBooks = MutableStateFlow(listOf<Book>())
    val books = mutableBooks.asStateFlow()

    private val chaptersMap = hashMapOf<String, List<Chapter>>()
    private val chapterMap = hashMapOf<String, Chapter>()
    private val blocksMap = hashMapOf<String, List<Block>>()

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

            val newBook = book.copy(chapters = chapterList)
            newBookList.add(newBook)

            chapterList.forEach { chapter ->
                val blockList = Blocks.load(context, chapter.path, injectMap)
                blocksMap[chapter.id] = blockList
                chapterMap[chapter.id] = chapter
            }
        }

        bookList = newBookList.toList()
        loaded = true

        observe(dao.chapterFlow()) { progress ->
            if (progress.isEmpty()) {
                mutableBooks.value = bookList
                return@observe
            }

            val progressMap = progress.associateBy { it.id }
            newBookList.clear()

            bookList.forEach { book ->
                val newChapterList = mutableListOf<Chapter>()
                book.chapters.forEach { chapter ->

                    val newChapter = chapter.copy(
                        learned = progressMap[chapter.id]?.learned ?: false,
                        new = !progressMap.containsKey(chapter.id)
                    )
                    newChapterList.add(newChapter)
                }
                val newBook = book.copy(
                    chapters = newChapterList,
                    new = newChapterList.any { it.new }
                )
                newBookList.add(newBook)
                chaptersMap[book.id] = newChapterList
            }

            bookList = newBookList.toList()
            mutableBooks.value = bookList
        }
    }

    fun getChapters(bookId: String) = chaptersMap[bookId] ?: emptyList()
    //fun getChapter(chapterId: String) = chapterMap[chapterId] ?: Chapter(chapterId)
    fun getBlocks(chapterId: String) = blocksMap[chapterId] ?: emptyList()
}