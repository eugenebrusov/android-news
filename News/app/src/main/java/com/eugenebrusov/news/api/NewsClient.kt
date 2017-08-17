package com.eugenebrusov.news.api

import com.eugenebrusov.news.models.NewsResponse
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by Eugene Brusov on 8/17/17.
 */
interface NewsClient {
    @GET("search?api-key=42b6624f-060e-4ee1-8218-32fcfcf3e8c1")
    fun getNews() : Call<NewsResponse>
}