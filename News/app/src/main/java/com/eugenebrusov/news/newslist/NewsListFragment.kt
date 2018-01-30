package com.eugenebrusov.news.newslist

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eugenebrusov.news.newsdetail.NewsDetailActivity
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

        val adapter = NewsListAdapter(viewModel)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        viewModel.openNewsDetailsEvent.observe(
                this@NewsListFragment,
                Observer<Int> { position ->
                    if (position != null) {
                        val holder = binding.recyclerView.findViewHolderForLayoutPosition(position)
                                as NewsListAdapter.ViewHolder

                        val intent = Intent(context, NewsDetailActivity::class.java)
                        intent.putExtra(NewsDetailActivity.EXTRA_NEWS_ID, holder.binding.newsItem?.id)

                        val bundle = ActivityOptionsCompat
                                .makeSceneTransitionAnimation(
                                        activity,
                                        holder.binding.thumbnailImage,
                                        holder.binding.thumbnailImage.transitionName)
                                .toBundle()
                        startActivity(intent, bundle)
                    }
                })
    }

    override fun onResume() {
        super.onResume()
        binding.viewModel?.start()
    }
}
