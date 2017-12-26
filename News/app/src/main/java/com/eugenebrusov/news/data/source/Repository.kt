package com.eugenebrusov.news.data.source

import com.eugenebrusov.news.api.NewsRetriever
import com.eugenebrusov.news.models.NewsListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Eugene Brusov on 11/16/17.
 */
class Repository {

    fun getNews(callback: DataSource.LoadNewsListCallback) {
        NewsRetriever().getNews(object : Callback<NewsListResponse> {
            override fun onResponse(call: Call<NewsListResponse>?, response: Response<NewsListResponse>?) {
                if (response?.isSuccessful == true) {
                    val results = response?.body()?.response?.results
                    if (results != null) {
                        callback.onNewsListLoaded(results)
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