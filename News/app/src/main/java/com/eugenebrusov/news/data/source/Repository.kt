package com.eugenebrusov.news.data.source

import com.eugenebrusov.news.api.NewsRetriever
import com.eugenebrusov.news.models.NewsListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Concrete implementation to load news
 * from the data sources into a cache.
 */
class Repository(
        val remoteDataSource: DataSource,
        val localDataSource: DataSource
) : DataSource {

    private var cacheIsDirty = false

    /**
     * Gets news from cache, local data source (SQLite) or remote data source, whichever is
     * available first.
     *
     *
     * Note: [LoadNewsListCallback.onDataNotAvailable] is fired if all data sources fail to
     * get the data.
     */
    override fun getNews(callback: DataSource.LoadNewsListCallback) {
        if (cacheIsDirty) {
            remoteDataSource.getNews(callback)
        } else {
            localDataSource.getNews(callback)
        }
    }

    companion object {

        private var INSTANCE: Repository? = null

        @JvmStatic fun getInstance(remoteDataSource: DataSource,
                                   localDataSource: DataSource) =
                INSTANCE ?: synchronized(Repository::class.java) {
                    INSTANCE ?: Repository(remoteDataSource, localDataSource)
                            .also { INSTANCE = it }
                }
    }

}