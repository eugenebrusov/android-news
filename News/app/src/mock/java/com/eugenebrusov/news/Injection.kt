package com.eugenebrusov.news

import android.content.Context
import com.eugenebrusov.news.data.source.Repository
import com.eugenebrusov.news.data.source.local.Database
import com.eugenebrusov.news.data.source.remote.guardian.GuardianService
import com.eugenebrusov.news.data.source.remote.util.LiveDataCallAdapterFactory
import com.eugenebrusov.news.data.source.util.AppExecutors
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.Executors

/**
 * Created by eugene on 11/16/17.
 */
object Injection {

    fun provideRepository(context: Context): Repository {

        val executors = AppExecutors(Executors.newSingleThreadExecutor(),
                Executors.newFixedThreadPool(3))

        val database = Database.getInstance(context)

        val httpClient = OkHttpClient.Builder()
                .addInterceptor(object : Interceptor {

                    @Throws(IOException::class)
                    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {

                        val original = chain.request()
                        val originalHttpUrl = original.url()

                        val url = originalHttpUrl.newBuilder()
                                .addQueryParameter("api-key", Constants.API_KEY)
                                .build()

                        val requestBuilder = original.newBuilder()
                                .url(url)

                        val request = requestBuilder.build()
                        return chain.proceed(request)
                    }
                })
                .build()

        val guardianService = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build()
                .create(GuardianService::class.java)

        return Repository.getInstance(executors,
                database.dao(),
                guardianService)
    }

}