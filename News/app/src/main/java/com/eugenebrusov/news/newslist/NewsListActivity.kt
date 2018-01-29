package com.eugenebrusov.news.newslist

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.eugenebrusov.news.R
import com.eugenebrusov.news.ViewModelFactory
import kotlinx.android.synthetic.main.activity_news_list.*

class NewsListActivity : AppCompatActivity() {

    private lateinit var viewModel : NewsListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_list)

        setSupportActionBar(toolbar)
    }

    fun obtainViewModel(): NewsListViewModel =
            ViewModelProviders.of(this,
                    ViewModelFactory.getInstance(application)).get(NewsListViewModel::class.java)

}
