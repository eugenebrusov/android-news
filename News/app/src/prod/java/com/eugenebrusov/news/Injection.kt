package com.eugenebrusov.news

import android.content.Context
import com.eugenebrusov.news.data.source.Repository
import com.eugenebrusov.news.data.source.local.Database
import com.eugenebrusov.news.data.source.local.LocalDataSource
import com.eugenebrusov.news.data.source.remote.RemoteDataSource
import com.eugenebrusov.news.util.AppExecutors

/**
 * Enables injection of production implementations for
 * [DataSource] at compile time.
 */
object Injection {

    fun provideRepository(context: Context): Repository {
        val database = Database.getInstance(context)
        return Repository.getInstance(RemoteDataSource,
                LocalDataSource.getInstance(AppExecutors(), database.dao()))
    }

}