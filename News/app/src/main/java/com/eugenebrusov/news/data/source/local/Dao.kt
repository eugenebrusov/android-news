package com.eugenebrusov.news.data.source.local

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.eugenebrusov.news.data.model.NewsItem
import com.eugenebrusov.news.data.model.NewsSection

/**
 * Data Access Object for the news table
 */
@Dao interface Dao {

    /**
     * Select all the news sections
     *
     * @return all news sections
     */
    @Query("SELECT * FROM sections ORDER BY webTitle")
    fun sections(): LiveData<List<NewsSection>>

    /**
     * Insert news sections in the database
     * If the news sections already exists, replace it
     *
     * @param newsSections the news items list to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewsSections(newsSections: List<NewsSection>)

    /**
     * Select all the news items for the specified section name
     *
     * @return all news items
     */
    @Query("SELECT * FROM news WHERE sectionId = :section ORDER BY webPublicationDate DESC")
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
     * Delete news items by section name
     *
     */
    @Query("DELETE FROM news WHERE sectionId = :section")
    fun deleteNews(section: String)

    /**
     * Select news item by given id
     *
     * @return news item by given id
     */
    @Query("SELECT * FROM news WHERE id = :id")
    fun findNewsItem(id: String): LiveData<NewsItem>
}