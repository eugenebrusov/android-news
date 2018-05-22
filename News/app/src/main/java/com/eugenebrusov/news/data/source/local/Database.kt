package com.eugenebrusov.news.data.source.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.eugenebrusov.news.data.model.NewsItem

/**
 * The Room Database that contains the NewsItem table.
 */
@Database(entities = arrayOf(NewsItem::class), version = 1)
abstract class Database : RoomDatabase() {

    abstract fun dao(): Dao

    companion object {

        private var INSTANCE: com.eugenebrusov.news.data.source.local.Database? = null

        @JvmStatic fun getInstance(context: Context) =
                INSTANCE ?: synchronized(Database::class.java) {
                    INSTANCE ?: Room.databaseBuilder(context.applicationContext,
                            com.eugenebrusov.news.data.source.local.Database::class.java,
                            "News.db")
                            .build()
                }
    }
}