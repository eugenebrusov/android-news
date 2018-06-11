package com.eugenebrusov.news.newslist

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
import com.eugenebrusov.news.data.model.Status.*
import com.eugenebrusov.news.databinding.ActivityNewsListBinding
import com.eugenebrusov.news.newsdetail.NewsDetailActivity
import kotlinx.android.synthetic.main.activity_news_list.*

class NewsListActivity : AppCompatActivity(), NewsListResultsFragment.OnNewsItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityNewsListBinding>(
                this, R.layout.activity_news_list)
                .apply {
                    viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
                    tabLayout.setupWithViewPager(viewPager)

                    setLifecycleOwner(this@NewsListActivity)
                    viewModel = ViewModelFactory
                            .obtainViewModel(this@NewsListActivity, NewsListViewModel::class.java)
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

    class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        var sectionsResource: Resource<List<NewsSection>>? = null
            set(value) {
                if (value == null) {
                    return
                }

                field = value
                notifyDataSetChanged()
            }

        override fun getCount(): Int {
            return when (sectionsResource?.status) {
                SUCCESS -> sectionsResource?.data?.size ?: 0
                LOADING -> 1
                ERROR -> 1
                else -> 0
            }
        }

        override fun getItem(position: Int): Fragment {
            return when (sectionsResource?.status) {
                SUCCESS -> NewsListResultsFragment.newInstance(sectionsResource?.data?.get(position)?.id!!)
                LOADING -> NewsListLoadingFragment()
                ERROR -> NewsListLoadingFragment()
                else -> throw IllegalArgumentException("Unknown resource state")
            }
        }

        override fun getPageTitle(position: Int): CharSequence {
            return when (sectionsResource?.status) {
                SUCCESS -> sectionsResource?.data?.get(position)?.webTitle ?: ""
                LOADING -> ""
                ERROR -> ""
                else -> throw IllegalArgumentException("Unknown resource state")
            }
        }

        override fun getItemPosition(`object`: Any): Int {
            return if (`object` is NewsListLoadingFragment && LOADING != sectionsResource?.status) {
                POSITION_NONE
            } else {
                POSITION_UNCHANGED
            }
        }
    }

}
