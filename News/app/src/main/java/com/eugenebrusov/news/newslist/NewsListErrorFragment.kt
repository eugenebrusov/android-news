package com.eugenebrusov.news.newslist

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eugenebrusov.news.ViewModelFactory
import com.eugenebrusov.news.databinding.FragmentNewsListErrorBinding

/**
 * Exposes error state on news list
 */
class NewsListErrorFragment : Fragment() {

    private lateinit var binding: FragmentNewsListErrorBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentNewsListErrorBinding
                .inflate(inflater, container, false)
                .apply {
                    setLifecycleOwner(this@NewsListErrorFragment)
                }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModel = ViewModelFactory.obtainViewModel(activity!!, NewsListViewModel::class.java)
        binding.viewModel = viewModel
    }
}