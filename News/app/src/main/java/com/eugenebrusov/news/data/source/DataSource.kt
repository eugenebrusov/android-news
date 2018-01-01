package com.eugenebrusov.news.data.source

import com.eugenebrusov.news.models.NewsResult

/**
 * Main entry point for accessing news data.
 *
 */
interface DataSource {

    interface LoadNewsListCallback {

        fun onNewsListLoaded(items: List<NewsResult>)

        fun onDataNotAvailable()
    }
}