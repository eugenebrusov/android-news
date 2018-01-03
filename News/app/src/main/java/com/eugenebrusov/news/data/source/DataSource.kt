package com.eugenebrusov.news.data.source

/**
 * Main entry point for accessing news data.
 *
 */
interface DataSource {

    interface LoadNewsListCallback {

        fun onNewsListLoaded(items: List<NewsItem>)

        fun onDataNotAvailable()
    }

    fun getNews(callback: LoadNewsListCallback)
}