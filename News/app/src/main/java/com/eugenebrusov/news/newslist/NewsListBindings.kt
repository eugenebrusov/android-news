package com.eugenebrusov.news.newslist

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.eugenebrusov.news.models.NewsResult
import com.eugenebrusov.news.models.NewsTag
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by eugene on 11/20/17.
 */
object NewsListBindings {

    @BindingAdapter("app:items")
    @JvmStatic fun setItems(recyclerView: RecyclerView, items: List<NewsResult>?) {
        with(recyclerView.adapter as NewsListAdapter) {
            replaceData(items)
        }
    }

    @BindingAdapter("app:thumbnail")
    @JvmStatic fun setThumbnail(imageView: ImageView, thumbnail: String) {
        Glide.with(imageView.context).load(thumbnail).into(imageView)
    }

    @BindingAdapter("app:byline")
    @JvmStatic fun setByline(imageView: ImageView, newsTag: NewsTag?) {
        Glide.with(imageView.context)
                .load(newsTag?.bylineImageUrl)
                .apply(RequestOptions().circleCrop())
                .into(imageView)
    }

    @BindingAdapter("app:webTitle")
    @JvmStatic fun setWebTitle(textView: TextView, newsTag: NewsTag?) {
        textView.text = newsTag?.webTitle
    }

    @BindingAdapter("app:webPublicationDate")
    @JvmStatic fun setWebPublicationDate(textView: TextView, webPublicationDate: String?) {
        val date: Date? =
                try {
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                            .parse(webPublicationDate)
                } catch (e: ParseException) { null }
        val formattedDate: String? =
                try {
                    SimpleDateFormat("MMM d, yyyy", Locale.US).format(date)
                } catch (e: ParseException) { null }
        textView.text = formattedDate
    }

}