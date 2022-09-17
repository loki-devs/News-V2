package com.example.newsource.repository

import com.example.core.data.remote.response.NewsResponse
import com.example.newsource.data.remote.service.NewsSourceService
import com.example.newsource.data.repository.NewsSourceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.BDDMockito
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class NewsSourceRepositoryTest : KoinTest {

    @Mock
    lateinit var apiService: NewsSourceService

    private val repository by inject<NewsSourceRepository>()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        startKoin {
            modules(module {
                single { NewsSourceRepository(apiService) }
            })
        }
    }

    @After
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun `on get news source by category success`() {
        runTest {
            val expectedResult = NewsResponse("1", 1, arrayListOf())
            BDDMockito.given(apiService.getNewsSourceByCategory("1", 1)).willReturn(expectedResult)
            val actual = repository.getNewsSourceByCategory("1", 1)
            assertEquals(expectedResult, actual)
        }
    }
}