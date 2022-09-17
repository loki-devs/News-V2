package com.example.article.data.repository

import com.example.article.data.remote.service.ArticleService
import com.example.core.data.remote.response.NewsResponse
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

class ArticleRepositoryTest : KoinTest {

    @Mock
    lateinit var apiService: ArticleService

    private val repository by inject<ArticleRepository>()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        startKoin {
            modules(module {
                single { ArticleRepository(apiService) }
            })
        }
    }

    @After
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun `on get article list success`() {
        runTest {
            val expectedResult = NewsResponse("1", 1, arrayListOf())
            BDDMockito.given(apiService.getArticlesBySource(1)).willReturn(expectedResult)
            val actual = repository.getArticlesBySource(1)
            assertEquals(expectedResult, actual)
        }
    }
}