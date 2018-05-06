package com.eugenebrusov.news.data.source.remote

import com.eugenebrusov.news.Constants
import com.eugenebrusov.news.data.NewsItem
import com.eugenebrusov.news.data.source.DataSource
import com.eugenebrusov.news.data.source.remote.models.NewsListResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

/**
 * Concrete implementation of the data source pulling data from REST API
 */
object RemoteDataSource : DataSource {

    private val service = Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient().newBuilder().build())
            .build()
            .create(Service::class.java)

    fun getNewsBefore(timestamp: Long, section: String, callback: DataSource.LoadNewsListCallback) {
        val webPublicationDate: String =
                try {
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                            .format(Date(timestamp))
                } catch (e: ParseException) { "" }
        service.getNews(webPublicationDate, section).enqueue(processResults(callback))
    }

    fun getNews(section: String, callback: DataSource.LoadNewsListCallback) {
        service.getNews(section).enqueue(processResults(callback))
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

    private fun processResults(callback: DataSource.LoadNewsListCallback) = object : Callback<NewsListResponse> {
        override fun onResponse(call: Call<NewsListResponse>?, response: Response<NewsListResponse>?) {
            if (response?.isSuccessful == true) {
                val results = response.body()?.response?.results
                if (results != null && results.isNotEmpty()) {
                    val items = mutableListOf<NewsItem>()
                    results.forEach {
                        val timestamp: Long =
                                try {
                                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                                            .parse(it.webPublicationDate)?.time ?: -1
                                } catch (e: ParseException) { -1 }

                        // Avoid adding items with empty or corrupted webPublicationDate
                        // since that parameter has major usage across the app
                        if (timestamp.compareTo(-1) != 0) {
                            val newsItem = NewsItem().apply {
                                if (it.id != null) {
                                    id = it.id
                                }

                                webPublicationDate = timestamp
                                sectionName = it.sectionName?.toLowerCase()
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
    }
}