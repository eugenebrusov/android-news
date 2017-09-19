package com.eugenebrusov.news


import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.eugenebrusov.news.models.NewsResult
import kotlinx.android.synthetic.main.fragment_news_detail.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewsDetailFragment : Fragment(), LifecycleRegistryOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)

    private var viewModel : NewsDetailViewModel? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_news_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(NewsDetailViewModel::class.java)
        val id = arguments?.getString(NEWS_ID)
        if (id != null) {
            viewModel?.newsResult(id)?.observe(this, Observer<NewsResult> { response ->
                val fields = response?.fields

                Glide.with(this).load(fields?.thumbnail).into(thumbnail_image)
                headline_text.text = fields?.headline

                val tag = if (response?.tags?.isNotEmpty() == true) response.tags[0] else null

                val date: Date? = try {
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).parse(response?.webPublicationDate)} catch (e: ParseException) { null }
                val webPublicationDate: String? = try {SimpleDateFormat("MMM d, yyyy", Locale.US).format(date)} catch (e: ParseException) { null }

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

        val NEWS_ID = "id"

        fun newInstance(id: String): NewsDetailFragment {
            val fragment = NewsDetailFragment()

            val bundle = Bundle()
            bundle.putString(NEWS_ID, id)
            fragment.arguments = bundle

            return fragment
        }

    }
}
