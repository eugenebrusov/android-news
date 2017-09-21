package com.eugenebrusov.news

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.eugenebrusov.news.models.NewsResult
import kotlinx.android.synthetic.main.activity_news_detail.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class NewsDetailActivity : AppCompatActivity(), LifecycleRegistryOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)

    private var viewModel : NewsDetailViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val imageUrl = intent.getStringExtra(NEWS_IMAGE_URL)
        Glide.with(this).load(imageUrl).into(thumbnail_image)

        viewModel = ViewModelProviders.of(this).get(NewsDetailViewModel::class.java)
        val id = intent.getStringExtra(NEWS_ID)
        if (id != null) {
            viewModel?.newsResult(id)?.observe(this, Observer<NewsResult> { response ->
                val fields = response?.fields

                headline_text.text = fields?.headline

                val tag = if (response?.tags?.isNotEmpty() == true) response.tags[0] else null

                val date: Date? = try {
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).parse(response?.webPublicationDate)} catch (e: ParseException) { null }
                val webPublicationDate: String? = try {
                    SimpleDateFormat("MMM d, yyyy", Locale.US).format(date)} catch (e: ParseException) { null }

                if (tag?.webTitle?.isNotEmpty() == true) {
                    byline_image.visibility = View.VISIBLE
                    web_title_text.visibility = View.VISIBLE
                    web_publication_date_text.visibility = View.VISIBLE

                    if (tag.bylineImageUrl?.isNotEmpty() == true) {
                        Glide.with(this).load(tag.bylineImageUrl).apply(RequestOptions().circleCrop()).into(byline_image)
                    } else {
                        byline_image.setImageResource(R.drawable.ic_person_black_24dp)
                    }

                    web_title_text.text = tag?.webTitle
                    web_publication_date_text.text = webPublicationDate
                } else {
                    byline_image.visibility = View.GONE
                    web_title_text.visibility = View.GONE
                    web_publication_date_text.visibility = View.GONE
                }

                var bodyText = fields?.bodyText?.replace(". ", ".\n\n")
                body_text.text = bodyText
            })
        }
    }

    override fun getLifecycle(): LifecycleRegistry {
        return lifecycleRegistry
    }

    companion object {
        const val NEWS_ID = "news_id"
        const val NEWS_IMAGE_URL = "news_image_url"
    }
}
