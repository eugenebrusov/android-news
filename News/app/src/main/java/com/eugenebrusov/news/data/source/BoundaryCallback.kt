package com.eugenebrusov.news.data.source

import android.arch.paging.PagedList
import android.arch.paging.PagingRequestHelper
import android.support.annotation.MainThread
import android.util.Log
import com.eugenebrusov.news.data.NewsItem
import com.eugenebrusov.news.data.source.local.LocalDataSource
import com.eugenebrusov.news.data.source.remote.RemoteDataSource
import java.util.concurrent.Executors

class BoundaryCallback(
        private val section: String,
        private val localDataSource: LocalDataSource,
        private val remoteDataSource: RemoteDataSource)
    : PagedList.BoundaryCallback<NewsItem>() {

    val ioExecutor = Executors.newSingleThreadExecutor()
    val helper = PagingRequestHelper(ioExecutor)

    @MainThread
    override fun onZeroItemsLoaded() {
        Log.e("boundaryCallback", "onZeroItemsLoaded()")
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            remoteDataSource.getNews(section, object : DataSource.LoadNewsListCallback {
                override fun onNewsListLoaded(items: List<NewsItem>) {
                    ioExecutor.execute {
                        Log.e("boundaryCallback", "onNewsListLoaded() " + items.get(0).sectionName)
                        localDataSource.insertNewsItems(items)
                        it.recordSuccess()
                    }
                }

                override fun onDataNotAvailable() {
                    Log.e("boundaryCallback", "onDataNotAvailable()")
                    it.recordFailure(Throwable("onDataNotAvailable"))
                }
            })
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: NewsItem) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            remoteDataSource.getNewsBefore(itemAtEnd.webPublicationDate, section, object : DataSource.LoadNewsListCallback {
                override fun onNewsListLoaded(items: List<NewsItem>) {
                    ioExecutor.execute {
                        Log.e("boundaryCallback", "onNewsListLoaded()")
                        localDataSource.insertNewsItems(items)
                        it.recordSuccess()
                    }
                }

                override fun onDataNotAvailable() {
                    Log.e("boundaryCallback", "onDataNotAvailable()")
                    it.recordFailure(Throwable("onDataNotAvailable"))
                }
            })
        }
    }
}