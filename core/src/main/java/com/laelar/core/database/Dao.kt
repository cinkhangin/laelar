package com.laelar.core.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.laelar.core.models.BlockData
import com.laelar.core.models.Chapter
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chapter: Chapter)

    @Update
    suspend fun update(chapter: Chapter)

    @Delete
    suspend fun delete(chapter: Chapter)

    @Query("SELECT * FROM chapters")
    suspend fun getChapters(): List<Chapter>

    @Query("SELECT * FROM chapters")
    fun chaptersFlow(): Flow<List<Chapter>>

    @Query("SELECT * FROM chapters WHERE id = :id")
    suspend fun getChapter(id: String): Chapter?

    @Query("SELECT * FROM chapters WHERE id = :id")
    fun chapterFlow(id: String): Flow<Chapter?>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(block: BlockData)

    @Update
    suspend fun update(block: BlockData)

    @Delete
    suspend fun delete(block: BlockData)

    @Query("SELECT * FROM blocks")
    fun blocksFlow(): Flow<List<BlockData>>

    @Query("SELECT * FROM blocks WHERE id = :id")
    suspend fun getBlock(id: String): BlockData?

    @Query("SELECT * FROM blocks WHERE id = :id")
    fun blockFlow(id: String): Flow<BlockData?>
}