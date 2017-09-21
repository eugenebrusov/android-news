package com.eugenebrusov.news

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_news_detail.*

class NewsDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val imageUrl = intent.getStringExtra(NewsDetailFragment.NEWS_IMAGE_URL)
        Glide.with(this).load(imageUrl).into(thumbnail_image)

        if (savedInstanceState == null) {
            val fragment = NewsDetailFragment
                    .newInstance(intent.getStringExtra(NewsDetailFragment.NEWS_ID), imageUrl)

            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment).commit()
        }
    }
}
