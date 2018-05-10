package com.eugenebrusov.news.data.source

import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.eugenebrusov.news.data.NewsItem
import com.eugenebrusov.news.data.source.local.LocalDataSource
import com.eugenebrusov.news.data.source.model.Resource
import com.eugenebrusov.news.data.source.remote.RemoteDataSource
import com.eugenebrusov.news.data.source.remote.models.NewsListResponse
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Concrete implementation to load news from the data sources.
 */
class Repository(
        val remoteDataSource: DataSource,
        val localDataSource: DataSource
) : DataSource {

    var cachedNewsItems: LinkedHashMap<String, NewsItem> = LinkedHashMap()

    private var cacheIsDirty = false

    fun loadNews(section: String): LiveData<PagedList<NewsItem>> {
        // create a boundary callback which will observe when the user reaches to the edges of
        // the list and update the database with extra data
        val boundaryCallback = BoundaryCallback(section,
                localDataSource = localDataSource as LocalDataSource,
                remoteDataSource = remoteDataSource as RemoteDataSource)

        // create a data source factory from Room
        val dataSourceFactory = localDataSource.loadNews(section)
        val builder = LivePagedListBuilder(dataSourceFactory, 20)
                .setBoundaryCallback(boundaryCallback)

        return builder.build()
    }

    fun searchNews(section: String): LiveData<Resource<List<NewsItem>>> {
        return object : PagedListNetworkBoundResource<List<NewsItem>, NewsListResponse>() {

            override fun processResponse(response: NewsListResponse): List<NewsItem> {

                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)

                return response.response?.results?.mapNotNull {
                    try {
                        val id = it.id ?: throw ParseException("Invalid news item id", 0)
                        val webPublicationDate = format.parse(it.webPublicationDate).time

                        var webTitle: String? = null
                        var bylineImageUrl: String? = null
                        if (it.tags?.isNotEmpty() == true) {
                            webTitle = it.tags[0].webTitle
                            bylineImageUrl = it.tags[0].bylineImageUrl
                        }

                        NewsItem(id = id,
                                webPublicationDate =  webPublicationDate,
                                sectionName = it.sectionName,
                                headline = it.fields?.headline,
                                trailText = it.fields?.trailText,
                                thumbnail = it.fields?.thumbnail,
                                bodyText = it.fields?.bodyText,
                                webTitle = webTitle,
                                bylineImageUrl = bylineImageUrl)
                    } catch (e: ParseException) {
                        null
                    }
                } ?: listOf()
            }

            override fun saveCallResult(item: List<NewsItem>) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun shouldFetch(data: List<NewsItem>?): Boolean {
                return true
            }

            //override fun loadFromDb() = (localDataSource as LocalDataSource).loadNews(section)

            override fun createCall() = (remoteDataSource as RemoteDataSource).searchNews(section)

            override fun onFetchFailed() {
                //repoListRateLimit.reset(owner)
            }
        }.asLiveData()
    }

//    /**
//     * Gets news from cache, local data source (SQLite) or remote data source, whichever is
//     * available first.
//     *
//     *
//     * Note: [LoadNewsListCallback.onDataNotAvailable] is fired if all data sources fail to
//     * get the data.
//     */
//    override fun getNews(callback: DataSource.LoadNewsListCallback) {
//        // Respond immediately with cache if available and not dirty
//        if (cachedNewsItems.isNotEmpty() && !cacheIsDirty) {
//            callback.onNewsListLoaded(ArrayList(cachedNewsItems.values))
//            return
//        }
//
//        if (cacheIsDirty) {
//            // If the cache is dirty we need to fetch new data from the network.
//            remoteDataSource.getNews(object : DataSource.LoadNewsListCallback {
//                override fun onNewsListLoaded(items: List<NewsItem>) {
//                    refreshCache(items)
//                    refreshLocalDataSource(items)
//                    callback.onNewsListLoaded(ArrayList(cachedNewsItems.values))
//                }
//
//                override fun onDataNotAvailable() {
//                    callback.onDataNotAvailable()
//                }
//            })
//        } else {
//            // Query the local storage if available. If not, query the network.
//            localDataSource.getNews(object : DataSource.LoadNewsListCallback {
//                override fun onNewsListLoaded(items: List<NewsItem>) {
//                    refreshCache(items)
//                    callback.onNewsListLoaded(items)
//                }
//
//                override fun onDataNotAvailable() {
//                    remoteDataSource.getNews(object : DataSource.LoadNewsListCallback {
//                        override fun onNewsListLoaded(items: List<NewsItem>) {
//                            refreshCache(items)
//                            refreshLocalDataSource(items)
//                            callback.onNewsListLoaded(ArrayList(cachedNewsItems.values))
//                        }
//
//                        override fun onDataNotAvailable() {
//                            callback.onDataNotAvailable()
//                        }
//                    })
//                }
//
//            })
//        }
//    }

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