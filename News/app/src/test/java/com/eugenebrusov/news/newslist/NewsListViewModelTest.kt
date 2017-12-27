package com.eugenebrusov.news.newslist

import android.app.Application
import com.eugenebrusov.news.data.source.DataSource
import com.eugenebrusov.news.data.source.Repository
import com.eugenebrusov.news.models.NewsResult
import com.eugenebrusov.news.newslist.util.capture
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.verify

/**
 * Created by Eugene Brusov on 12/25/17.
 */
class NewsListViewModelTest {

    @Mock private lateinit var context: Application
    @Mock private lateinit var repository: Repository
    @Captor private lateinit var loadNewsListCallbackCaptor: ArgumentCaptor<DataSource.LoadNewsListCallback>
    private lateinit var newsListViewModel: NewsListViewModel
    private lateinit var items: List<NewsResult>

    @Before
    fun setupViewModel() {
        // To inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this)

        // Get a reference to the class under test
        newsListViewModel = NewsListViewModel(context, repository)

        // Initialise the NewsResult array
        items = listOf(NewsResult(), NewsResult(), NewsResult())
    }

    @Test
    fun loadNewsListFromRepository_dataLoaded() {
        with(newsListViewModel) {
            // When the view model is asked to load data
            loadNews()

            // Error view is hidden
            assertFalse(dataError.get())

            // Progress indicator is shown
            assertTrue(dataLoading.get())

            // Callback is captured and invoked with stubbed data
            verify(repository).getNews(capture(loadNewsListCallbackCaptor))
            loadNewsListCallbackCaptor.value.onNewsListLoaded(this@NewsListViewModelTest.items)

            // Data loaded
            assertTrue(items.size == this@NewsListViewModelTest.items.size)

            // Progress indicator is hidden
            assertFalse(dataLoading.get())

            // Error view is still hidden
            assertFalse(dataError.get())
        }
    }

}