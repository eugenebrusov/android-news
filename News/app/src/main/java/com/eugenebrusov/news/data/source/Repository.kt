package com.eugenebrusov.news.data.source

import java.util.LinkedHashMap
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.forEach
import kotlin.collections.isNotEmpty

/**
 * Concrete implementation to load news
 * from the data sources into a cache.
 */
class Repository(
        val remoteDataSource: DataSource,
        val localDataSource: DataSource
) : DataSource {

    var cachedNewsItem: LinkedHashMap<String, NewsItem> = LinkedHashMap()

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
        // Respond immediately with cache if available and not dirty
        if (cachedNewsItem.isNotEmpty() && !cacheIsDirty) {
            callback.onNewsListLoaded(ArrayList(cachedNewsItem.values))
            return
        }

        if (cacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            remoteDataSource.getNews(callback)
        } else {
            // Query the local storage if available. If not, query the network.
            localDataSource.getNews(object : DataSource.LoadNewsListCallback {
                override fun onNewsListLoaded(items: List<NewsItem>) {
                    callback.onNewsListLoaded(items)
                }

                override fun onDataNotAvailable() {
                    remoteDataSource.getNews(object : DataSource.LoadNewsListCallback {
                        override fun onNewsListLoaded(items: List<NewsItem>) {
                            refreshCache(items)
                            refreshLocalDataSource(items)
                            callback.onNewsListLoaded(ArrayList(cachedNewsItem.values))
                        }

                        override fun onDataNotAvailable() {
                            callback.onDataNotAvailable()
                        }
                    })
                }

            })
        }
    }

    private fun refreshCache(news: List<NewsItem>) {
        cachedNewsItem.clear()
        news.forEach {
            cachedNewsItem.put(it.id, it)
        }
        cacheIsDirty = false
    }

    private fun refreshLocalDataSource(news: List<NewsItem>) {
        // TODO implement
    }

    companion object {

        private var INSTANCE: Repository? = null

        @JvmStatic fun getInstance(remoteDataSource: DataSource,
                                   localDataSource: DataSource) =
                INSTANCE ?: synchronized(Repository::class.java) {
                    INSTANCE ?: Repository(remoteDataSource, localDataSource)
                            .also { INSTANCE = it }
                }

        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }

}