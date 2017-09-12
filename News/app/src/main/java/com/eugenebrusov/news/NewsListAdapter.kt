package com.eugenebrusov.news

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.eugenebrusov.news.models.NewsResults
import kotlinx.android.synthetic.main.item_news.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Eugene Brusov on 8/18/17.
 */
class NewsListAdapter(val newsClickListener: OnNewsClickListener) : RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {

    interface OnNewsClickListener {
        fun onNewsSelected(id: String)
    }

    var newsResults: NewsResults? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return newsResults?.results?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_news, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val result = newsResults?.results?.get(position)

        val fields = result?.fields

        Glide.with(holder?.itemView?.context).load(fields?.thumbnail).into(holder?.thumbnailImageView)
        holder?.headlineTextView?.text = fields?.headline

        val tag = if (result?.tags?.isNotEmpty() == true) result.tags[0] else null

        Glide.with(holder?.itemView?.context).load(tag?.bylineImageUrl).apply(RequestOptions().circleCrop()).into(holder?.bylineImageView)

        holder?.webTitleTextView?.text = tag?.webTitle

        val date: Date? = try {SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).parse(result?.webPublicationDate)} catch (e: ParseException) { null }
        holder?.webPublicationDate?.text = try {SimpleDateFormat("MMM d, yyyy", Locale.US).format(date)} catch (e: ParseException) { null }

        holder?.itemView?.setOnClickListener {
            val id = result?.id
            if (id != null) {
                newsClickListener.onNewsSelected(id)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnailImageView: ImageView = itemView.thumbnail_image
        val headlineTextView: TextView = itemView.headline_text
        val bylineImageView: ImageView = itemView.byline_image
        val webTitleTextView: TextView = itemView.web_title_text
        val webPublicationDate: TextView = itemView.web_publication_date_text
    }
}