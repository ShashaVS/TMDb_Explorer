package com.shashavs.tmdbexplorer.ui.view_model

import android.arch.paging.DataSource
import com.shashavs.tmdbexplorer.repository.AppRepository
import com.shashavs.tmdbexplorer.repository.data_objects.Movie
import org.junit.Before
import org.junit.Test

import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.junit.Assert.*
import org.mockito.Mockito.*

@RunWith(MockitoJUnitRunner::class)
class MovieViewModelTest {

    @Mock
    private lateinit var repository: AppRepository
    @Mock
    private lateinit var dataSource: DataSource.Factory<Int, Movie>
    private lateinit var viewModel: MovieViewModel
    private val apiKey = "apiKey"
    private val testQuery = "test_query"
    private val testMovieId = 123

    @Before
    fun setUp() {
        // set Stubbing methods to repository
        `when`(repository.dataSourceFactory()).thenReturn(dataSource)
        `when`(repository.query()).thenReturn(testQuery)

        viewModel = MovieViewModel(repository)
        viewModel.initDataSourceLiveData(apiKey)
    }

    @Test
    fun initDataSourceLiveData() {
        assertNotNull(viewModel.pagedListLiveData)
    }

    @Test
    fun refresh() {
        viewModel.refresh(apiKey)
        verify(repository).loadPage(apiKey, true)
    }

    @Test
    fun search() {
        viewModel.search(apiKey, testQuery)
        verify(repository).search(apiKey, testQuery)
    }

    @Test
    fun getQuery() {
        val query = viewModel.getQuery()
        verify(repository).query()
        assertEquals(testQuery, query)
    }

    @Test
    fun getDetails() {
        viewModel.getDetails(apiKey, testMovieId)
        verify(repository).getDetails(testMovieId, apiKey)
    }

    @Test
    fun refreshLiveData() {
        viewModel.refreshLiveData()
        verify(repository).getRefreshLiveData()
    }

    @Test
    fun clear() {
        viewModel.clear()
        verify(repository).clear()
        assertNull(viewModel.pagedListLiveData)
    }
}