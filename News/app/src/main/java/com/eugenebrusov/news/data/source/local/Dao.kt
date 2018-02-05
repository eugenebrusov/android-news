package com.eugenebrusov.news.data.source.local

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.eugenebrusov.news.data.source.NewsItem

/**
 * Data Access Object for the news table
 */
@Dao interface Dao {

    @Query("SELECT * FROM news WHERE headline LIKE :request")
    fun loadNews(request: String) : DataSource.Factory<Int, NewsItem>

    /**
     * Select all news from the news table
     *
     * @return all news
     */
    @Query("SELECT * FROM news")
    fun getNews(): List<NewsItem>

    /**
     * Insert a news item in the database
     * If the news item already exists, replace it
     *
     * @param task the news item to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewsItem(newsItem: NewsItem)

    /**
     * Delete all news
     */
    @Query("DELETE FROM news")
    fun deleteNews()
}