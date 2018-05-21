package com.eugenebrusov.news.data.source

import com.eugenebrusov.news.data.NewsItem

/**
 * Main entry point for accessing news data
 *
 */
interface DataSource {

    interface LoadNewsListCallback {

        fun onNewsListLoaded(items: List<NewsItem>)

        fun onDataNotAvailable()
    }

    interface LoadNewsItemCallback {

        fun onNewsItemLoaded(item: NewsItem)

        fun onDataNotAvailable()
    }

    fun saveNewsItems(newsItems: List<NewsItem>)

    fun deleteAllNews()
}