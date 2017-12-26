package com.eugenebrusov.news.newslist

import android.app.Application
import com.eugenebrusov.news.data.source.DataSource
import com.eugenebrusov.news.data.source.Repository
import com.eugenebrusov.news.newslist.util.capture
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

    @Before
    fun setupViewModel() {
        MockitoAnnotations.initMocks(this)

        newsListViewModel = NewsListViewModel(context, repository)
    }

    @Test
    fun loadNewsListFromRepositoryAndLoadIntoView() {
        newsListViewModel.loadNews()

        verify(repository).getNews(capture(loadNewsListCallbackCaptor))
    }
}