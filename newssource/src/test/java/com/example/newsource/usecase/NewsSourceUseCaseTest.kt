package com.example.newsource.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.core.ErrorCode
import com.example.core.data.remote.response.NewsResponse
import com.example.core.domain.Either
import com.example.core.presentation.viewstate.ViewError
import com.example.newsource.domain.model.NewsSourceData
import com.example.newsource.domain.model.NewsSourceParam
import com.example.newsource.domain.source.NewsSourceDataSource
import com.example.newsource.domain.usecase.NewsSourceUseCase
import com.example.newsource.domain.usecase.NewsSourceUseCaseImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.BDDMockito
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.io.IOException

class NewsSourceUseCaseTest : KoinTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val useCase by inject<NewsSourceUseCase>()

    @Mock
    private lateinit var repository: NewsSourceDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        startKoin {
            modules(module {
                single {
                    NewsSourceUseCaseImpl(repository) as NewsSourceUseCase
                }
            })
        }
    }

    @After
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun `success get news source by category`() {
        runTest {
            val expected = Either.Success(listOf(NewsSourceData("1", "1")))
            BDDMockito.given(repository.getNewsSourceByCategory("1",1)).willReturn(
                NewsResponse(
                    "1", 1,
                    listOf(
                        NewsResponse.Articles(
                            NewsResponse.Source("1", "1"),
                            "author",
                            "title",
                            "desc",
                            "url",
                            "urlImage",
                            "publish",
                            "content"
                        )
                    )
                )
            )

            val actual = useCase.invoke(NewsSourceParam("1", 1))

            assertEquals(expected, actual)
        }
    }

    @Test
    fun `internal server error get news source by category`() {
        runTest {
            val expected = Either.Fail(ViewError(ErrorCode.GLOBAL_UNKNOWN_ERROR))
            BDDMockito.given(repository.getNewsSourceByCategory("1", 1)).willAnswer { throw RuntimeException() }

            val actual = useCase.invoke(NewsSourceParam("1", 1))

            assertEquals(expected, actual)
        }
    }

    @Test
    fun `IO error get news source by category`() {
        runTest {
            val expected = Either.Fail(ViewError(ErrorCode.GLOBAL_INTERNET_ERROR))
            BDDMockito.given(repository.getNewsSourceByCategory("1", 1)).willAnswer { throw IOException() }

            val actual = useCase.invoke(NewsSourceParam("1", 1))

            assertEquals(expected, actual)
        }
    }
}