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

    override fun onNewsSelected(id: String, imageUrl: String?) {
        val intent = Intent(this, NewsDetailActivity::class.java)
        intent.putExtra(NewsDetailFragment.NEWS_ID, id)
        intent.putExtra(NewsDetailFragment.NEWS_IMAGE_URL, imageUrl)
        startActivity(intent)
    }
}
