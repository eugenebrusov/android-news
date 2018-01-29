package com.eugenebrusov.news.data.source

import com.eugenebrusov.news.newslist.util.any
import com.eugenebrusov.news.newslist.util.capture
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.atLeastOnce
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
    private var items: List<NewsItem> = listOf(NewsItem(), NewsItem(), NewsItem())

    @Before
    fun setupRepository() {
        // To inject the mocks in the test the initMocks method needs to be called
        MockitoAnnotations.initMocks(this)

        // Get a reference to the class under test
        repository = Repository.getInstance(remoteDataSource, localDataSource)
    }

    @After
    fun destroyRepository() {
        Repository.destroyInstance()
    }

    @Test
    fun getNews_requestsNewsListFromLocalDataSource() {
        // When news list is requested from the news repository
        repository.getNews(loadNewsListCallback)

        // Then news list is loaded from the local data source
        verify(localDataSource).getNews(any<DataSource.LoadNewsListCallback>())
    }

    @Test
    fun getNewsWithLocalDataSourceUnavailable_newsListRetrievedFromRemote() {
        // When news list is requested from the news repository
        repository.getNews(loadNewsListCallback)

        // Then news list is failed to load from localDataSource,
        // callback is captured to invoke onDataNotAvailable
        verify(localDataSource).getNews(capture(newsListCallbackCaptor))

        // The local data source has no data available
        newsListCallbackCaptor.value.onDataNotAvailable()

        // The remote data source has data available
        verify(remoteDataSource).getNews(capture(newsListCallbackCaptor))
        newsListCallbackCaptor.value.onNewsListLoaded(items)

        // Verify the news from the remote data source are returned
        verify(loadNewsListCallback).onNewsListLoaded(items)
    }

    @Test
    fun getNews_repositoryCachesAfterFirstApiCall() {
        // When news list is requested from the news repository
        repository.getNews(loadNewsListCallback) // First call to API

        // Capture callback to invoke onDataNotAvailable,
        // since localDataSource doesn't have any data at the very beginning
        verify(localDataSource).getNews(capture(newsListCallbackCaptor))
        newsListCallbackCaptor.value.onDataNotAvailable()

        // Verify the remote data source is queried
        verify(remoteDataSource).getNews(capture(newsListCallbackCaptor))

        // Trigger callback so tasks are cached
        newsListCallbackCaptor.value.onNewsListLoaded(items)

        // Request news list again
        // to check data returned from cache and not from local or remote data sources
        repository.getNews(loadNewsListCallback) // Second call to API

        // Verify the remoteDataSource isn't invoked more than once
        verify(remoteDataSource).getNews(any<DataSource.LoadNewsListCallback>())

        // Verify the localDataSource isn't invoked more than once
        verify(localDataSource).getNews(any<DataSource.LoadNewsListCallback>())

        // Verify the news from the cache are returned
        verify(loadNewsListCallback, atLeastOnce()).onNewsListLoaded(items)
    }

}