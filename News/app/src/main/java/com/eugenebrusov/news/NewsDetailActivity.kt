package com.eugenebrusov.news

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class NewsDetailActivity : AppCompatActivity() {

    companion object {
        val NEWS_ID = "id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
    }
}
