package com.eugenebrusov.news.newslist

import org.junit.Test

/**
 * Created by Eugene Brusov on 12/25/17.
 */
class NewsListViewModelTest {
    @Test
    fun loadNewsListFromRepositoryAndLoadIntoView() {
        newsListViewModel.loadNews()
    }
}