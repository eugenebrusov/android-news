package com.eugenebrusov.news.api

import com.eugenebrusov.news.Constants
import com.eugenebrusov.news.models.NewsDetailResponse
import com.eugenebrusov.news.models.NewsListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Eugene Brusov on 8/17/17.
 */
interface NewsClient {
    @GET("search?api-key=${Constants.API_KEY}&show-tags=contributor&show-fields=all&show-refinements=all")
    fun getNews(@Query("page") page: Int) : Call<NewsListResponse>
}