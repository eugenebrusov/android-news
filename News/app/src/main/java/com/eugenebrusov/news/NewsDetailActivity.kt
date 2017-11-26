package com.eugenebrusov.news

import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_news_detail.*

class NewsDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        }

//        val result = intent.getParcelableExtra<NewsResult>(NEWS_RESULT)
//
//        val fields = result?.fields
//
//        Glide.with(this).load(fields?.thumbnail).into(thumbnail_image)
//
//        headline_text.text = fields?.headline
//
//        val tag = if (result?.tags?.isNotEmpty() == true) result.tags[0] else null
//
//        val date: Date? = try {
//            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).parse(result?.webPublicationDate)} catch (e: ParseException) { null }
//        val webPublicationDate: String? = try {
//            SimpleDateFormat("MMM d, yyyy", Locale.US).format(date)} catch (e: ParseException) { null }
//
//        if (tag?.webTitle?.isNotEmpty() == true) {
//            byline_image.visibility = View.VISIBLE
//            web_title_text.visibility = View.VISIBLE
//            web_publication_date_text.visibility = View.VISIBLE
//
//            if (tag.bylineImageUrl?.isNotEmpty() == true) {
//                Glide.with(this).load(tag.bylineImageUrl).apply(RequestOptions().circleCrop()).into(byline_image)
//            } else {
//                byline_image.setImageResource(R.drawable.ic_person_black_24dp)
//            }
//
//            web_title_text.text = tag?.webTitle
//            web_publication_date_text.text = webPublicationDate
//        } else {
//            byline_image.visibility = View.GONE
//            web_title_text.visibility = View.GONE
//            web_publication_date_text.visibility = View.GONE
//        }
//
//        var bodyText = fields?.bodyText?.replace(". ", ".\n\n")
//        body_text.text = bodyText
    }

    companion object {
        const val NEWS_RESULT = "news_result"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> finishAfterTransition()
        }
        return true
    }
}
