package com.eugenebrusov.news

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.annotation.VisibleForTesting
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.eugenebrusov.news.data.source.Repository
import com.eugenebrusov.news.newsdetail.NewsDetailViewModel
import com.eugenebrusov.news.newslist.NewsListViewModel

/**
 * Created by eugene on 11/16/17.
 */
class ViewModelFactory private constructor(
        private val application: Application,
        private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(NewsListViewModel::class.java) ->
                        NewsListViewModel(application, repository)
                    isAssignableFrom(NewsDetailViewModel::class.java) ->
                        NewsDetailViewModel(application, repository)
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile private var INSTANCE: ViewModelFactory? = null

        private fun getInstance(application: Application) =
                INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                    INSTANCE ?: ViewModelFactory(application,
                            Injection.provideRepository(application.applicationContext))
                            .also { INSTANCE = it }
                }

        fun <T : ViewModel?> obtainViewModel(fragment: Fragment, modelClass: Class<T>) =
                ViewModelProviders.of(fragment,
                        ViewModelFactory.getInstance(fragment.activity?.application!!)).get(modelClass)

        fun <T : ViewModel?> obtainViewModel(activity: FragmentActivity, modelClass: Class<T>) =
                ViewModelProviders.of(activity,
                        ViewModelFactory.getInstance(activity.application)).get(modelClass)

        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}