package com.eugenebrusov.news

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class NewsDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            val fragment = NewsDetailFragment
                    .newInstance(intent.getStringExtra(NewsDetailFragment.NEWS_ID))

            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment).commit()
        }
    }
}
