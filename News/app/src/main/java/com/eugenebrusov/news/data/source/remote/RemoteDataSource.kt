package com.eugenebrusov.news.data.source.remote

import com.eugenebrusov.news.data.source.DataSource

/**
 * Concrete implementation of the data source pulling data from REST API
 */
object RemoteDataSource : DataSource {

    override fun getNews(callback: DataSource.LoadNewsListCallback) {

    }
}