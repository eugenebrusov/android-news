package com.eugenebrusov.news

import android.content.Context
import com.eugenebrusov.news.data.source.Repository

/**
 * Created by eugene on 11/16/17.
 */
object Injection {

    fun provideRepository(context: Context): Repository {
        // TODO implement Repository with parameters
        return Repository()
    }

}