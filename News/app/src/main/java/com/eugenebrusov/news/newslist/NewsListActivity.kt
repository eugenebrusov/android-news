package com.eugenebrusov.news.newslist

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.eugenebrusov.news.R
import com.eugenebrusov.news.ViewModelFactory
import com.eugenebrusov.news.newsdetail.NewsDetailActivity
import kotlinx.android.synthetic.main.activity_news_list.*

class NewsListActivity : AppCompatActivity(), OnNewsItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_list)

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

}
