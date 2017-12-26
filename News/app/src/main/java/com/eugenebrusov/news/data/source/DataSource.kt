package com.eugenebrusov.news.data.source

import com.eugenebrusov.news.models.NewsResult

/**
 * Created by Eugene Brusov on 12/25/17.
 */
class DataSource {
    interface LoadNewsListCallback {

        fun onNewsListLoaded(items: List<NewsResult>)

        fun onDataNotAvailable()
    }
}