package com.eugenebrusov.news.newslist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.eugenebrusov.news.NewsDetailActivity
import com.eugenebrusov.news.R
import com.eugenebrusov.news.ViewModelFactory
import com.eugenebrusov.news.models.NewsResult
import kotlinx.android.synthetic.main.activity_news_list.*

class NewsListActivity : AppCompatActivity(), NewsListFragment.OnNewsClickListener {

    private lateinit var viewModel : NewsListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_list)

        setSupportActionBar(toolbar)

        viewModel = obtainViewModel().apply {
            openNewsDetailsEvent.observe(this@NewsListActivity, Observer<String> { newsId ->
                if (newsId?.isNotEmpty() == true) {
                    openNewsDetails(newsId)
                }
            })
        }
    }

    override fun onNewsSelected(result: NewsResult, sharedImage: ImageView) {
        val intent = Intent(this, NewsDetailActivity::class.java)
        intent.putExtra(NewsDetailActivity.NEWS_RESULT, result)

        val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedImage, sharedImage.transitionName).toBundle()
        startActivity(intent, bundle)
    }

    private fun openNewsDetails(newsId: String) {
        val intent = Intent(this, NewsDetailActivity::class.java)
        intent.putExtra(NewsDetailActivity.NEWS_RESULT, newsId)

        startActivity(intent)
//        val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedImage, sharedImage.transitionName).toBundle()
//        startActivity(intent, bundle)
    }

    fun obtainViewModel():NewsListViewModel =
            ViewModelProviders.of(this, ViewModelFactory.getInstance(application)).get(NewsListViewModel::class.java)

}
