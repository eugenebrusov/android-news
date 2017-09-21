package com.eugenebrusov.news

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.eugenebrusov.news.models.NewsResult
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NewsListFragment.OnNewsClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
    }

    override fun onNewsSelected(result: NewsResult) {
        val intent = Intent(this, NewsDetailActivity::class.java)
        intent.putExtra(NewsDetailActivity.NEWS_RESULT, result)
        startActivity(intent)
    }
}
