package com.eugenebrusov.news.newslist

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eugenebrusov.news.databinding.FragmentNewsListBinding


class NewsListFragment : Fragment() {

    interface OnNewsItemSelectedListener {
        fun onNewsItemSelected(id: String, sharedView: View)
    }

    var newsItemSelectedCallback: OnNewsItemSelectedListener? = null

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

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.addItemDecoration(NewsListSpacesDecoration())
        binding.recyclerView.adapter = NewsListPagedAdapter { newsItemId, sharedView ->
            newsItemSelectedCallback?.onNewsItemSelected(newsItemId, sharedView)
        }

        viewModel.openNewsDetailsEvent.observe(
                this,
                Observer { position ->
                    if (position != null) {
//                        val holder = binding.recyclerView.findViewHolderForLayoutPosition(position)
//                                as NewsListAdapter.ViewHolder
//
//                        val intent = Intent(context, NewsDetailActivity::class.java)
//                        intent.putExtra(NewsDetailActivity.EXTRA_NEWS_ID, holder.binding.newsItem?.id)
//
//                        val bundle = ActivityOptionsCompat
//                                .makeSceneTransitionAnimation(
//                                        activity as NewsListActivity,
//                                        holder.binding.thumbnailImage,
//                                        holder.binding.thumbnailImage.transitionName)
//                                .toBundle()
//                        startActivity(intent, bundle)
                    }
                })

        viewModel.loadNews("politics")
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            newsItemSelectedCallback = context as OnNewsItemSelectedListener?
        } catch (e: ClassCastException) {
            throw ClassCastException(context!!.toString() + " must implement OnNewsItemSelectedListener")
        }
    }

}
