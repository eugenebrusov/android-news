package com.eugenebrusov.news.newslist

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eugenebrusov.news.ViewModelFactory
import com.eugenebrusov.news.databinding.FragmentNewsListResultsBinding

private const val ARG_SECTION = "section"

/**
 * Exposes news list results
 */
class NewsListResultsFragment : Fragment() {

    private lateinit var binding: FragmentNewsListResultsBinding
    private lateinit var section: String

    private var listener: OnNewsItemSelectedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            section = it.getString(ARG_SECTION)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentNewsListResultsBinding
                .inflate(inflater, container, false)
                .apply {
                    setLifecycleOwner(this@NewsListResultsFragment)
                }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModel = ViewModelFactory.obtainViewModel(this, NewsListViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.loadNews(section = section)

        val recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(NewsListSpacesDecoration())

        val adapter = NewsListPagedAdapter { id, sharedView ->
            listener?.onNewsItemSelected(id, sharedView)
        }
        recyclerView.adapter = adapter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnNewsItemSelectedListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnNewsItemSelectedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     * Probably, interaction between fragment and activity should be arranged via shared ViewModel,
     * but since it's required to transfer sharedView it's better to send it via interface
     * rather than via LiveData
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnNewsItemSelectedListener {
        fun onNewsItemSelected(id: String, sharedView: View)
    }

    companion object {
        /**
         * Used to create a new instance of NewsListResultsFragment
         * with given section name.
         *
         * @param section Selected news category.
         * @return A new instance of fragment NewsListResultsFragment.
         */
        @JvmStatic fun newInstance(section: String) =
                NewsListResultsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_SECTION, section)
                    }
                }
    }
}
