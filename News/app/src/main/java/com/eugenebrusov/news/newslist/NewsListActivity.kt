package com.eugenebrusov.news.newslist

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.eugenebrusov.news.R
import com.eugenebrusov.news.ViewModelFactory
import com.eugenebrusov.news.data.model.NewsSection
import com.eugenebrusov.news.data.model.Resource
import com.eugenebrusov.news.data.model.Status.SUCCESS
import com.eugenebrusov.news.data.model.Status.ERROR
import com.eugenebrusov.news.data.model.Status.LOADING
import com.eugenebrusov.news.databinding.ActivityNewsListBinding
import com.eugenebrusov.news.newsdetail.NewsDetailActivity
import kotlinx.android.synthetic.main.activity_news_list.*

class NewsListActivity : AppCompatActivity(), OnNewsItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityNewsListBinding>(
                this, R.layout.activity_news_list)
                .apply {
                    viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
                    tabLayout.setupWithViewPager(viewPager)

                    setLifecycleOwner(this@NewsListActivity)
                    viewModel = obtainViewModel()
                }

        setSupportActionBar(toolbar)
    }

    override fun onNewsItemSelected(id: String, sharedView: View) {

        val intent = Intent(this, NewsDetailActivity::class.java)
        intent.putExtra(NewsDetailActivity.EXTRA_NEWS_ID, id)

        val bundle = ActivityOptionsCompat
                .makeSceneTransitionAnimation(
                        this,
                        sharedView,
                        sharedView.transitionName)
                .toBundle()
        startActivity(intent, bundle)
    }

    fun obtainViewModel(): NewsListViewModel =
            ViewModelProviders.of(this,
                    ViewModelFactory.getInstance(application)).get(NewsListViewModel::class.java)

    class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        var pagesResource: Resource<List<NewsSection>>? = null
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun getCount(): Int {
            return when (pagesResource?.status) {
                SUCCESS -> pagesResource?.data?.size ?: 0
                LOADING -> 1
                ERROR -> 1
                else -> 0
            }
        }

        override fun getItem(i: Int): Fragment {
            return when (pagesResource?.status) {
                SUCCESS -> NewsListFragment()
                LOADING -> NewsListLoadingFragment()
                ERROR -> NewsListLoadingFragment()
                else -> throw IllegalArgumentException("Unknown resource state")
            }
        }

        override fun getPageTitle(position: Int): CharSequence {
            return when (pagesResource?.status) {
                SUCCESS -> pagesResource?.data?.get(position)?.webTitle ?: ""
                LOADING -> ""
                ERROR -> ""
                else -> throw IllegalArgumentException("Unknown resource state")
            }
        }

        override fun getItemPosition(`object`: Any): Int {
            return POSITION_NONE
        }
    }

}
