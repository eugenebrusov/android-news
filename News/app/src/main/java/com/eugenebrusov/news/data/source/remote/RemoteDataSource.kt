package com.eugenebrusov.news.data.source.remote

import com.eugenebrusov.news.api.NewsRetriever
import com.eugenebrusov.news.data.source.DataSource
import com.eugenebrusov.news.data.source.NewsItem
import com.eugenebrusov.news.models.NewsListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Concrete implementation of the data source pulling data from REST API
 */
object RemoteDataSource : DataSource {

    override fun getNews(callback: DataSource.LoadNewsListCallback) {
        NewsRetriever().getNews(object : Callback<NewsListResponse> {
            override fun onResponse(call: Call<NewsListResponse>?, response: Response<NewsListResponse>?) {
                if (response?.isSuccessful == true) {
                    val results = response.body()?.response?.results
                    if (results != null && results.isNotEmpty()) {
                        val items = mutableListOf<NewsItem>()
                        results.forEach {
                            val newsItem = NewsItem().apply {
                                id = it.id ?: ""
                                webPublicationDate = it.webPublicationDate ?: ""
                                headline = it.fields?.headline ?: ""
                                trailText = it.fields?.trailText ?: ""
                                thumbnail = it.fields?.thumbnail ?: ""
                                bodyText = it.fields?.bodyText ?: ""
                                if (it.tags?.isNotEmpty() == true) {
                                    webTitle = it.tags[0].webTitle ?: ""
                                    bylineImageUrl = it.tags[0].bylineImageUrl ?: ""
                                }
                            }
                            items.add(newsItem)
                        }
                        callback.onNewsListLoaded(items)
                    } else {
                        callback.onDataNotAvailable()
                    }
                } else {
                    callback.onDataNotAvailable()
                }
            }

            override fun onFailure(call: Call<NewsListResponse>?, t: Throwable?) {
                callback.onDataNotAvailable()
            }
        })
    }
}