package com.eugenebrusov.news.data.source.local

import com.eugenebrusov.news.data.source.DataSource
import com.eugenebrusov.news.data.model.NewsItem
import com.eugenebrusov.news.util.AppExecutors

/**
 * Concrete implementation of the data source pulling data from local database
 */
class LocalDataSource private constructor(
        val appExecutors: AppExecutors,
        val dao: Dao
) : DataSource {

    fun getNews(callback: DataSource.LoadNewsListCallback) {
        appExecutors.diskIO.execute {
            val news = dao.getNews()
            appExecutors.mainThread.execute {
                if (news.isEmpty()) {
                    // This will be called if the table is new or just empty
                    callback.onDataNotAvailable()
                } else {
                    callback.onNewsListLoaded(news)
                }
            }
        }
    }

    override fun saveNewsItems(newsItems: List<NewsItem>) {
        appExecutors.diskIO.execute {
            newsItems.forEach {
                dao.insertNewsItem(it)
            }
        }
    }

    override fun deleteAllNews() {
        appExecutors.diskIO.execute {
            dao.deleteNews()
        }
    }

    fun insertNewsItems(newsItems: List<NewsItem>) {
        appExecutors.diskIO.execute {
            dao.insertNewsItems(newsItems)
        }
    }

    fun searchNews(section: String): android.arch.paging.DataSource.Factory<Int, NewsItem> {
        return dao.searchNews(section)
    }

    fun loadNews(section: String): android.arch.paging.DataSource.Factory<Int, NewsItem> {
        return dao.loadNews(section)
    }

    companion object {

        private var INSTANCE: LocalDataSource? = null

        @JvmStatic fun getInstance(appExecutors: AppExecutors,
                                   dao: Dao) =
                INSTANCE ?: synchronized(LocalDataSource::class.java) {
                    INSTANCE ?: LocalDataSource(appExecutors, dao)
                            .also { INSTANCE = it }
                }
    }
}