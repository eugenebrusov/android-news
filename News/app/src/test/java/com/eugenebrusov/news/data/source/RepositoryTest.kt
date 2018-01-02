package com.eugenebrusov.news.data.source

import com.eugenebrusov.news.models.NewsResult
import com.eugenebrusov.news.newslist.util.any
import com.eugenebrusov.news.newslist.util.capture
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Unit tests for the implementation of the in-memory repository with cache.
 */
class RepositoryTest {

    private lateinit var repository: Repository
    @Mock private lateinit var remoteDataSource: DataSource
    @Mock private lateinit var localDataSource: DataSource
    @Mock private lateinit var loadNewsListCallback: DataSource.LoadNewsListCallback
    @Captor private lateinit var newsListCallbackCaptor:
            ArgumentCaptor<DataSource.LoadNewsListCallback>
    private var items: List<NewsResult> = listOf(NewsResult(), NewsResult(), NewsResult())

    @Before
    fun setupRepository() {
        // To inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this)

        // Get a reference to the class under test
        repository = Repository.getInstance(remoteDataSource, localDataSource)
    }

    @Test
    fun getNews_repositoryCachesAfterFirstApiCall() {
        // When news are requested from repository
        repository.getNews(loadNewsListCallback) // First call to API

        // Use the Mockito Captor to capture the callback
        verify(localDataSource).getNews(capture(newsListCallbackCaptor))

        // Local data source doesn't have data yet
        newsListCallbackCaptor.value.onDataNotAvailable()

        // Verify the remote data source is queried
        verify(remoteDataSource).getNews(capture(newsListCallbackCaptor))

        // Trigger callback so news are cached
        newsListCallbackCaptor.value.onNewsListLoaded(items)

        repository.getNews(loadNewsListCallback) // Second call to API

        // Then news were only requested once from Service API
        verify(remoteDataSource).getNews(any<DataSource.LoadNewsListCallback>())
    }

    @Test
    fun getNews_requestsNewsListFromLocalDataSource() {
        // When news list is requested from the news repository
        repository.getNews(loadNewsListCallback)

        // Then news list is loaded from the local data source
        verify(localDataSource).getNews(any<DataSource.LoadNewsListCallback>())
    }

}