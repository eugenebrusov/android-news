package com.eugenebrusov.news.data.source

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import com.eugenebrusov.news.data.NewsItem
import com.eugenebrusov.news.data.source.local.LocalDataSource
import com.eugenebrusov.news.data.source.model.Resource
import com.eugenebrusov.news.data.source.remote.RemoteDataSource
import com.eugenebrusov.news.data.source.remote.models.NewsListResponse
import com.eugenebrusov.news.data.source.remote.util.ApiResponse
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

    fun searchNews(section: String): LiveData<Resource<PagedList<NewsItem>>> {
        return object : PagedListNetworkBoundResource<List<NewsItem>, NewsListResponse>() {

            override fun dataSourceFactory(): android.arch.paging.DataSource.Factory<Int, NewsItem> {
                return (localDataSource as LocalDataSource).searchNews(section)
            }

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
                                sectionName = it.sectionName?.toLowerCase(),
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

            override fun saveCallResult(items: List<NewsItem>) {
                (localDataSource as LocalDataSource).saveNewsItems(items)
            }

            override fun shouldFetch(data: List<NewsItem>?): Boolean {
                return true
            }

            override fun createCall(timestamp: Long?): LiveData<ApiResponse<NewsListResponse>> {
                val toDate: String? =
                        if (timestamp != null) {
                            try {
                                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                                        .format(Date(timestamp))
                            } catch (e: ParseException) {
                                null
                            }
                        } else {
                            null
                        }

                return (remoteDataSource as RemoteDataSource).searchNews(section, toDate)
            }

            override fun onFetchFailed() {
                //repoListRateLimit.reset(owner)
            }
        }.asLiveData()
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