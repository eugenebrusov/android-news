package com.eugenebrusov.news

import android.arch.lifecycle.*
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eugenebrusov.news.models.News
import com.eugenebrusov.news.models.NewsList
import com.eugenebrusov.news.models.NewsResponse

class NewsListFragment : Fragment(), LifecycleRegistryOwner {

    private val LogTag = NewsListFragment::class.java.simpleName

    private val lifecycleRegistry = LifecycleRegistry(this)

    private var viewModel : NewsListViewModel? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_news_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(NewsListViewModel::class.java)
        viewModel?.newsList?.observe(this, Observer<NewsList> { response ->
            Log.d(LogTag,
                    "NewsListViewModel updated with ${response?.results?.size} items")
        })
    }

    override fun getLifecycle(): LifecycleRegistry {
        return lifecycleRegistry
    }
}
