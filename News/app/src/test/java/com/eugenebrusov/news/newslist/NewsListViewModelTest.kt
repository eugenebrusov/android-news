package com.eugenebrusov.news.newslist

import android.app.Application
import com.eugenebrusov.news.data.source.Repository
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Eugene Brusov on 12/25/17.
 */
class NewsListViewModelTest {

    @Mock private lateinit var context: Application
    @Mock private lateinit var repository: Repository
    private lateinit var newsListViewModel: NewsListViewModel

    @Before
    fun setupViewModel() {
        MockitoAnnotations.initMocks(this)

        newsListViewModel = NewsListViewModel(context, repository)
    }

    @Test
    fun loadNewsListFromRepositoryAndLoadIntoView() {
        newsListViewModel.loadNews()
    }
}