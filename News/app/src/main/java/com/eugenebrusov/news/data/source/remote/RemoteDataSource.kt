package com.eugenebrusov.news.data.source.remote

import com.eugenebrusov.news.Constants
import com.eugenebrusov.news.data.source.DataSource
import com.eugenebrusov.news.data.source.NewsItem
import com.eugenebrusov.news.data.source.remote.models.NewsListResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Concrete implementation of the data source pulling data from REST API
 */
object RemoteDataSource : DataSource {

    private val service = Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient().newBuilder().build())
            .build()
            .create(Service::class.java)

    override fun getNews(callback: DataSource.LoadNewsListCallback) {
        service.getNews().enqueue(object : Callback<NewsListResponse> {
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

    override fun getNewsItem(newsItemId: String, callback: DataSource.LoadNewsItemCallback) {
        // Not required for the remote data source
    }

    override fun saveNewsItems(newsItems: List<NewsItem>) {
        // Not required for the remote data source
    }

    override fun deleteAllNews() {
        // Not required for the remote data source
    }
}