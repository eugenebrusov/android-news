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
                    setLifecycleOwner(this@NewsListActivity)
                    viewModel = obtainViewModel()
                    viewPager.adapter = PagerAdapter(supportFragmentManager)
                    tabLayout.setupWithViewPager(viewPager)
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

    private class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getCount(): Int  = 10

        override fun getItem(i: Int): Fragment {
            val fragment = NewsListFragment()
            return fragment
        }

        override fun getPageTitle(position: Int): CharSequence {
            return "OBJECT " + (position + 1)
        }
    }

}
