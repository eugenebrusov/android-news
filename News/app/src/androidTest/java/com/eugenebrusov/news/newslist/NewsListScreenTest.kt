package com.eugenebrusov.news.newslist

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.eugenebrusov.news.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Eugene Brusov on 12/28/17.
 */
@RunWith(AndroidJUnit4::class) class NewsListScreenTest {

    @Rule @JvmField var newsListActivityTestRule =
            ActivityTestRule<NewsListActivity>(NewsListActivity::class.java)

    @Test
    fun tapNewsItem_opensNewsDetailUi() {
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition<NewsListAdapter.ViewHolder>(0, click()))

        onView(withId(R.id.details_scroll)).check(matches(isDisplayed()))
    }

}