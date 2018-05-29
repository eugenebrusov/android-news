package com.eugenebrusov.news.newsdetail

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.WindowManager
import com.eugenebrusov.news.R
import com.eugenebrusov.news.ViewModelFactory
import com.eugenebrusov.news.databinding.ActivityNewsDetailBinding
import kotlinx.android.synthetic.main.activity_news_detail.*

/**
 * Displays news details screen
 */
class NewsDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityNewsDetailBinding>(
                this, R.layout.activity_news_detail)
                .apply {
                    setLifecycleOwner(this@NewsDetailActivity)
                    viewModel = obtainViewModel()
                }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set transparent status bar on devices with SDK 21+
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
    }

    /**
     * Overriding back button tap and perform back transition with animation
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finishAfterTransition()
        }
        return true
    }

    fun obtainViewModel(): NewsDetailViewModel =
            ViewModelProviders.of(this,
                    ViewModelFactory.getInstance(application)).get(NewsDetailViewModel::class.java)

    companion object {

        const val EXTRA_NEWS_ID = "news_id"
    }
}
