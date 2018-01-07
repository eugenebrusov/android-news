package com.eugenebrusov.news.newsdetail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eugenebrusov.news.databinding.FragmentNewsDetailsBinding

class NewsDetailFragment : Fragment() {

    private lateinit var binding: FragmentNewsDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentNewsDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModel = (activity as NewsDetailActivity).obtainViewModel()
        binding.viewModel = viewModel
    }

    override fun onResume() {
        super.onResume()

        binding.viewModel?.start(activity.intent.getStringExtra(NewsDetailActivity.EXTRA_NEWS_ID))
    }
}
