package com.eugenebrusov.news.newslist

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.eugenebrusov.news.databinding.FragmentNewsListBinding
import com.eugenebrusov.news.models.NewsResult

class NewsListFragment : Fragment(), NewsListAdapter.OnNewsClickListener, NewsListAdapter.OnPageRequestedListener {

    interface OnNewsClickListener {
        fun onNewsSelected(result: NewsResult, sharedImage: ImageView)
    }

    private var newsClickListener: OnNewsClickListener? = null

    private lateinit var binding: FragmentNewsListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentNewsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModel = (activity as NewsListActivity).obtainViewModel()
        binding.viewModel = viewModel

        val adapter = NewsListAdapter(this, this, viewModel)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

//        viewModel = NewsListActivity.obtainViewModel(activity)
//        viewModel?.newsResults?.observe(this, Observer<NewsResults> { response ->
//            adapter.newsResults = response
//        })
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

    override fun onResume() {
        super.onResume()
        binding.viewModel?.start()
    }

    override fun onNewsSelected(result: NewsResult, sharedImage: ImageView) {
        newsClickListener?.onNewsSelected(result, sharedImage)
    }

    override fun onNextPageRequested() {
        //viewModel?.loadNextPage()
    }
}
