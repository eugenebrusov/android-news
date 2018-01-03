package com.eugenebrusov.news.data.source.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.eugenebrusov.news.data.source.NewsItem

/**
 * Data Access Object for the news table
 */
@Dao interface Dao {

    /**
     * Select all news from the news table.
     *
     * @return all news.
     */
    @Query("SELECT * FROM news") fun getNews(): List<NewsItem>
}