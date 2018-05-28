package com.eugenebrusov.news.newslist

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eugenebrusov.news.databinding.FragmentNewsListBinding

class NewsListFragment : Fragment() {

    private lateinit var binding: FragmentNewsListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentNewsListBinding.inflate(inflater, container, false).apply {
            setLifecycleOwner(this@NewsListFragment)
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModel = (activity as NewsListActivity).obtainViewModel()
        binding.viewModel = viewModel
        viewModel.loadNews("politics")

        val recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(NewsListSpacesDecoration())

        val adapter = NewsListPagedAdapter()
        recyclerView.adapter = adapter

        /**
         *
         * Probably, interaction between fragment and activity should be arranged via shared ViewModel,
         * but it might bring unexpected problems if LiveData handles values of View class
         * since it's required to transfer sharedView to animate transition between news list and new detail
         *
         */
        try {
            adapter.newsItemSelectedCallback = context as? OnNewsItemSelectedListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context!!.toString() + " must implement OnNewsItemSelectedListener")
        }
    }

}
