package com.eugenebrusov.news.data.source.local

import com.eugenebrusov.news.data.source.DataSource
import com.eugenebrusov.news.util.AppExecutors

/**
 * Concrete implementation of the data source pulling data from local database
 */
class LocalDataSource private constructor(
        val appExecutors: AppExecutors,
        val dao: Dao
) : DataSource {

    companion object {

        private var INSTANCE: LocalDataSource? = null

        @JvmStatic fun getInstance(appExecutors: AppExecutors,
                                   dao: Dao) =
                INSTANCE ?: synchronized(LocalDataSource::class.java) {
                    INSTANCE ?: LocalDataSource(appExecutors, dao)
                            .also { INSTANCE = it }
                }
    }
}