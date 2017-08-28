package com.eugenebrusov.news

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eugenebrusov.news.models.NewsResults

class NewsListFragment : Fragment(), LifecycleRegistryOwner {

    interface OnNewsClickListener {
        fun onNewsSelected(id: String)
    }

    private var newsClickListener: OnNewsClickListener? = null

    private val lifecycleRegistry = LifecycleRegistry(this)

    private var viewModel : NewsListViewModel? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_news_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = NewsListAdapter()

        val recyclerView = view as? RecyclerView
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = adapter

        viewModel = ViewModelProviders.of(this).get(NewsListViewModel::class.java)
        viewModel?.newsResults?.observe(this, Observer<NewsResults> { response ->
            adapter.newsResults = response
        })
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            newsClickListener = context as OnNewsClickListener
        }
        catch (e: ClassCastException) {
            throw ClassCastException(context?.toString() + " must implement OnNewsClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        newsClickListener = null
    }

    override fun getLifecycle(): LifecycleRegistry {
        return lifecycleRegistry
    }
}
