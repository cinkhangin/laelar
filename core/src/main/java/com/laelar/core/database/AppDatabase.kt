package com.laelar.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.laelar.core.models.BlockData
import com.laelar.core.models.Chapter

@Database(entities = [Chapter::class, BlockData::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): Dao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun create(context: Context): AppDatabase {
            val migration3to4 = object : Migration(3, 4) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL(
                        """
                        CREATE TABLE IF NOT EXISTS blocks (
                        id TEXT PRIMARY KEY NOT NULL,
                        version INTEGER NOT NULL)
                        """.trimIndent()
                    )
                }
            }

            return instance ?: synchronized(this) {
                val database = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "chapters"
                ).addMigrations(migration3to4)
                    .build()
                instance = database
                database
            }
        }
    }
}