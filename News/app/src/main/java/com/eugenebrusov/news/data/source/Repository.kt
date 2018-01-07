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

    var cachedNewsItems: LinkedHashMap<String, NewsItem> = LinkedHashMap()

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
        if (cachedNewsItems.isNotEmpty() && !cacheIsDirty) {
            callback.onNewsListLoaded(ArrayList(cachedNewsItems.values))
            return
        }

        if (cacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            remoteDataSource.getNews(object : DataSource.LoadNewsListCallback {
                override fun onNewsListLoaded(items: List<NewsItem>) {
                    refreshCache(items)
                    refreshLocalDataSource(items)
                    callback.onNewsListLoaded(ArrayList(cachedNewsItems.values))
                }

                override fun onDataNotAvailable() {
                    callback.onDataNotAvailable()
                }
            })
        } else {
            // Query the local storage if available. If not, query the network.
            localDataSource.getNews(object : DataSource.LoadNewsListCallback {
                override fun onNewsListLoaded(items: List<NewsItem>) {
                    refreshCache(items)
                    callback.onNewsListLoaded(items)
                }

                override fun onDataNotAvailable() {
                    remoteDataSource.getNews(object : DataSource.LoadNewsListCallback {
                        override fun onNewsListLoaded(items: List<NewsItem>) {
                            refreshCache(items)
                            refreshLocalDataSource(items)
                            callback.onNewsListLoaded(ArrayList(cachedNewsItems.values))
                        }

                        override fun onDataNotAvailable() {
                            callback.onDataNotAvailable()
                        }
                    })
                }

            })
        }
    }

    /**
     * Gets news item from local data source (sqlite) unless the table is new or empty. In that case it
     * uses the network data source.
     *
     *
     * Note: [LoadNewsItemCallback.onDataNotAvailable] is fired if both data sources fail to
     * get the data.
     */
    override fun getNewsItem(newsItemId: String, callback: DataSource.LoadNewsItemCallback) {
        val newsItemInCache = cachedNewsItems[newsItemId]

        // Respond immediately with cache if available
        if (newsItemInCache != null) {
            callback.onNewsItemLoaded(newsItemInCache)
        }

        // Try to load news item from local data source
        // If news item doesn't exist load from remote data source
        localDataSource.getNewsItem(newsItemId, object : DataSource.LoadNewsItemCallback {
            override fun onNewsItemLoaded(item: NewsItem) {
                callback.onNewsItemLoaded(item)
            }

            override fun onDataNotAvailable() {
                remoteDataSource.getNewsItem(newsItemId, object : DataSource.LoadNewsItemCallback{
                    override fun onNewsItemLoaded(item: NewsItem) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataNotAvailable() {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })
            }
        })
    }

    override fun saveNewsItems(newsItems: List<NewsItem>) {
        remoteDataSource.saveNewsItems(newsItems)
        localDataSource.saveNewsItems(newsItems)
    }

    override fun deleteAllNews() {
        remoteDataSource.deleteAllNews()
        localDataSource.deleteAllNews()
        cachedNewsItems.clear()
    }

    private fun refreshCache(news: List<NewsItem>) {
        cachedNewsItems.clear()
        news.forEach {
            cachedNewsItems.put(it.id, it)
        }
        cacheIsDirty = false
    }

    private fun refreshLocalDataSource(news: List<NewsItem>) {
        localDataSource.deleteAllNews()
        localDataSource.saveNewsItems(news)
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