package com.shashavs.tmdbexplorer.repository

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.shashavs.tmdbexplorer.repository.data_objects.Dates
import com.shashavs.tmdbexplorer.repository.data_objects.Movie
import com.shashavs.tmdbexplorer.repository.data_objects.PaginationData
import com.shashavs.tmdbexplorer.repository.retrofit.ApiService
import com.shashavs.tmdbexplorer.repository.room.AppDatabase
import com.shashavs.tmdbexplorer.repository.room.dao.MovieDao
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.json.JSONObject
import org.junit.Before
import org.junit.Test

import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.junit.Assert.*
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.Mockito.*
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class AppRepositoryTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var apiService: ApiService
    @Mock
    lateinit var appDatabase: AppDatabase
    @Mock
    lateinit var movieDao: MovieDao
    private lateinit var repository: AppRepository

    private val apiKey = "apiKey"
    private var testPage = 1
    private var testTotalPages = 2
    private var testQuery = "test_query"
    private var testMovieId = 123
    private lateinit var testResponse: Response<PaginationData<Movie>>
    private lateinit var testErrorResponse: Response<ResponseBody>
    private lateinit var testDetail: Response<Movie>
    private lateinit var testMovie: Movie

    @Before
    fun setUp() {

        repository = AppRepository(apiService, appDatabase)

        testMovie = Movie(testMovieId)

        testResponse = Response.success(
            PaginationData(testPage, testTotalPages, 10, Dates("max", "min"), listOf(testMovie))
        )

        val errorBody = JSONObject().apply {
            put("status_message", "message_Error")
        }
        testErrorResponse = Response.error(401,
            ResponseBody.create(MediaType.parse("application/json;charset=utf-8"), errorBody.toString())
        )

        testDetail = Response.success(testMovie)

        `when`(apiService.nowPlaying(apiKey, testPage.plus(1)))
            .thenReturn(Observable.fromCallable { testResponse })

        `when`(apiService.nowPlaying(apiKey, 1))
            .thenReturn(Observable.fromCallable { testResponse })

        `when`(apiService.search(apiKey, testPage.plus(1), testQuery))
            .thenReturn(Observable.fromCallable { testResponse })

        `when`(apiService.search(apiKey, 1, testQuery))
            .thenReturn(Observable.fromCallable { testResponse })

        `when`(apiService.getDetail(testMovieId, apiKey))
            .thenReturn(Observable.fromCallable { testDetail })

        `when`(appDatabase.movieDao()).thenReturn( movieDao )
    }

    /**
     * isRefresh = true
     */
    @Test
    fun loadPageRefresh() {
        repository.loadPage(apiKey, true)

        assertEquals(0, repository.page)
        assertNull(repository.totalPages)
        assertNull(repository.query)

        verify(apiService).nowPlaying(apiKey, 1)
        verify(apiService, times(0)).search(apiKey, 1, testQuery)
    }

    /**
     * isRefresh = false
     * query = null
     */
    @Test
    fun loadPageNoRefreshNoQuery() {
        repository.page = testPage
        repository.totalPages = testTotalPages
        repository.query = null
        repository.loadPage(apiKey, false)

        verify(apiService).nowPlaying(apiKey, testPage.plus(1))
        verify(apiService, times(0)).search(apiKey, testPage.plus(1), testQuery)
    }

    /**
     * isRefresh = false
     * query != null
     */
    @Test
    fun loadPageNoRefreshQuery() {
        repository.page = testPage
        repository.totalPages = testTotalPages
        repository.query = testQuery
        repository.loadPage(apiKey, false)

        verify(apiService, times(0)).nowPlaying(apiKey, testPage.plus(1))
        verify(apiService).search(apiKey, testPage.plus(1), testQuery)
    }

    /**
     * isRefresh = false
     * page = totalPages
     */
    @Test
    fun loadPageNoRefreshLastPage() {
        repository.page = testPage
        repository.totalPages = testPage
        repository.loadPage(apiKey, false)

        verify(apiService, times(0)).nowPlaying(apiKey, testPage.plus(1))
        verify(apiService, times(0)).search(apiKey, testPage.plus(1), testQuery)
    }

    @Test
    fun nowPlaying() {
        repository.page = testPage
        repository.nowPlaying(apiKey)

        verify(apiService).nowPlaying(apiKey, testPage.plus(1))
    }

    @Test
    fun search() {
        repository.page = testPage
        repository.query = null
        repository.search(apiKey, testQuery)

        assertEquals(testQuery, repository.query)
        assertEquals(0, repository.page)
        assertNull(repository.totalPages)
        verify(apiService).search(apiKey, 1, testQuery)
    }

    @Test
    fun getDetails() {
        repository.getDetails(testMovieId, apiKey)

        verify(apiService).getDetail(testMovieId, apiKey)
        verify(appDatabase.movieDao()).getMovieById(testMovieId)
    }

    @Test
    fun parseResponse() {
        repository.parseResponse(testResponse)

        assertEquals(testPage, repository.page)
        assertEquals(testTotalPages, repository.totalPages)
        verify(appDatabase).clearAllTables()
        verify(appDatabase.movieDao()).insertMovies(testResponse.body()?.results!!)
    }

    @Test
    fun parseDetails() {
        repository.parseDetails(testDetail)
        verify(appDatabase.movieDao()).updateMovie(testMovie)
    }

    @Test
    fun parseError() {
        repository.parseError(testErrorResponse.errorBody())
        assertEquals("message_Error", repository.getErrorLiveData().value)
    }

    @Test
    fun query() {
        repository.query = testQuery
        assertEquals(testQuery, repository.query())
    }

    @Test
    fun clear() {
        repository.page = testPage
        repository.totalPages = testTotalPages
        repository.query = testQuery
        repository.clear()

        assertNull(repository.page)
        assertNull(repository.totalPages)
        assertNull(repository.query)
    }
}