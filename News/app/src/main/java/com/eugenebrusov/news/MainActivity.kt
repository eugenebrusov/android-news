package com.eugenebrusov.news

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.eugenebrusov.news.models.NewsResult
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NewsListFragment.OnNewsClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
    }

    override fun onNewsSelected(result: NewsResult, sharedImage: ImageView) {
        val intent = Intent(this, NewsDetailActivity::class.java)
        intent.putExtra(NewsDetailActivity.NEWS_RESULT, result)

        val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedImage, sharedImage.transitionName).toBundle()
        startActivity(intent, bundle)
    }
}
