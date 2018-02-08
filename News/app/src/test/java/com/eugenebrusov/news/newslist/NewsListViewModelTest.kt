package com.eugenebrusov.news.newslist

import android.app.Application
import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.eugenebrusov.news.data.source.DataSource
import com.eugenebrusov.news.data.NewsItem
import com.eugenebrusov.news.data.source.Repository
import com.eugenebrusov.news.newslist.util.capture
import com.eugenebrusov.news.newslist.util.mock
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Created by Eugene Brusov on 12/25/17.
 */
class NewsListViewModelTest {

    @get:Rule var instantExecutorRule = InstantTaskExecutorRule()
    @Mock private lateinit var context: Application
    @Mock private lateinit var repository: Repository
    @Captor private lateinit var loadNewsListCallbackCaptor: ArgumentCaptor<DataSource.LoadNewsListCallback>
    private lateinit var newsListViewModel: NewsListViewModel
    private lateinit var items: List<NewsItem>

    @Before
    fun setupViewModel() {
        // To inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this)

        // Get a reference to the class under test
        newsListViewModel = NewsListViewModel(context, repository)

        // Initialise the NewsResult array
        items = listOf(NewsItem(), NewsItem(), NewsItem())
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

    @Test
    fun loadNewsListFromRepository_failedToLoadData() {
        with(newsListViewModel) {
            // When the view model is asked to load data
            loadNews()

            // Error view is hidden
            assertFalse(dataError.get())

            // Progress indicator is shown
            assertTrue(dataLoading.get())

            // Callback is captured and data not available callback invoked
            verify(repository).getNews(capture(loadNewsListCallbackCaptor))
            loadNewsListCallbackCaptor.value.onDataNotAvailable()

            // Items cleared
            assertTrue(items.size == 0)

            // Progress indicator is hidden
            assertFalse(dataLoading.get())

            // Error view is visible
            assertTrue(dataError.get())
        }
    }

    @Test
    fun clickOnNewsItem_ShowsDetailUi() {
        val observer = mock<Observer<Int>>()

        with(newsListViewModel) {
            openNewsDetailsEvent.observe(TestUtils.TEST_OBSERVER, observer)

            openNewsDetailsEvent.value = anyInt()
        }

        verify(observer).onChanged(anyInt())
    }
}