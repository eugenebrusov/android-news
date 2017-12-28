package com.eugenebrusov.news.newslist

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
        binding = FragmentNewsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModel = (activity as NewsListActivity).obtainViewModel()
        binding.viewModel = viewModel

        val adapter = NewsListAdapter(viewModel)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        binding.viewModel?.start()
    }
}
