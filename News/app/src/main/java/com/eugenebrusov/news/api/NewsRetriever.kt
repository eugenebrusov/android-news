package com.eugenebrusov.news.api

import com.eugenebrusov.news.models.NewsDetailResponse
import com.eugenebrusov.news.models.NewsListResponse
import okhttp3.OkHttpClient
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Eugene Brusov on 8/17/17.
 */
class NewsRetriever {
    private val client: NewsClient

    init {
        val retrofit = Retrofit.Builder().baseUrl("https://content.guardianapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient().newBuilder().build())
                .build()
        client = retrofit.create(NewsClient::class.java)
    }

    fun getNews(callback: Callback<NewsListResponse>) {
        client.getNews().enqueue(callback)
    }
}