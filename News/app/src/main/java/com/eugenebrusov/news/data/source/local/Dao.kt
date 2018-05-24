package com.eugenebrusov.news.data.source.local

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.eugenebrusov.news.data.model.NewsItem

/**
 * Data Access Object for the news table
 */
@Dao interface Dao {

    /**
     * Select all the news for the specified section name
     *
     * @return all news
     */
    @Query("SELECT * FROM news WHERE sectionName = :section ORDER BY webPublicationDate DESC")
    fun searchNews(section: String): DataSource.Factory<Int, NewsItem>

    /**
     * Insert news items in the database
     * If the news item already exists, replace it
     *
     * @param newsItems the news items list to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewsItems(newsItems: List<NewsItem>)

    /**
     * Select news item by given id
     *
     * @return news item by given id
     */
    @Query("SELECT * FROM news WHERE id = :id")
    fun findNewsItem(id: String): LiveData<NewsItem>
}