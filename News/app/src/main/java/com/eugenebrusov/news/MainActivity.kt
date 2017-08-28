package com.eugenebrusov.news

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NewsListFragment.OnNewsClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
    }

    override fun onNewsSelected(id: String) {
        val intent = Intent(this, NewsDetailActivity::class.java)
        intent.putExtra(NewsDetailActivity.NEWS_ID, id)
        startActivity(intent)
    }
}
