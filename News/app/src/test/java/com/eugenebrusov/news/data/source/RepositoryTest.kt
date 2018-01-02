package com.eugenebrusov.news.data.source

import com.eugenebrusov.news.newslist.util.any
import org.junit.Before
import org.junit.Test
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
    @Mock private lateinit var loadTasksCallback: DataSource.LoadNewsListCallback

    @Before
    fun setupRepository() {
        // To inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this)

        // Get a reference to the class under test
        repository = Repository.getInstance(remoteDataSource, localDataSource)
    }

    @Test
    fun getNews_requestsNewsListFromLocalDataSource() {
        // When news list is requested from the tasks repository
        repository.getNews(loadTasksCallback)

        // Then news list is loaded from the local data source
        verify<DataSource>(localDataSource).getNews(any<DataSource.LoadNewsListCallback>())
    }

}