package com.eugenebrusov.news.api

import com.eugenebrusov.news.models.NewsDetailResponse
import com.eugenebrusov.news.models.NewsListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Eugene Brusov on 8/17/17.
 */
interface NewsClient {
    @GET("search?api-key=42b6624f-060e-4ee1-8218-32fcfcf3e8c1&show-tags=contributor&show-fields=all&show-refinements=all")
    fun getNews() : Call<NewsListResponse>

    @GET("{id}?api-key=42b6624f-060e-4ee1-8218-32fcfcf3e8c1&show-tags=contributor&show-fields=all&show-refinements=all")
    fun getNewsDetail(@Path("id", encoded = true) id: String) : Call<NewsDetailResponse>
}