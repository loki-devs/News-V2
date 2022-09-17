package com.example.article.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.article.domain.model.ArticleData
import com.example.article.domain.model.ArticleParam
import com.example.article.domain.source.ArticleDataSource
import com.example.core.ErrorCode
import com.example.core.data.remote.response.NewsResponse
import com.example.core.domain.Either
import com.example.core.presentation.viewstate.ViewError
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

class ArticleUseCaseTest : KoinTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val useCase by inject<ArticleUseCase>()

    @Mock
    private lateinit var repository: ArticleDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        startKoin {
            modules(module {
                single {
                    ArticleUseCaseImpl(repository) as ArticleUseCase
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
    fun `success get articles by source`() {
        runTest {
            val expected = Either.Success(
                listOf(
                    ArticleData(
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
            BDDMockito.given(repository.getArticlesBySource(1)).willReturn(
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

            val actual = useCase.invoke(ArticleParam("1", 1))

            assertEquals(expected, actual)
        }
    }

    @Test
    fun `internal server error get articles by source`() {
        runTest {
            val expected = Either.Fail(ViewError(ErrorCode.GLOBAL_UNKNOWN_ERROR))
            BDDMockito.given(repository.getArticlesBySource(1)).willAnswer { throw RuntimeException() }

            val actual = useCase.invoke(ArticleParam("10", 1))

            assertEquals(expected, actual)
        }
    }

    @Test
    fun `IO error get articles by source`() {
        runTest {
            val expected = Either.Fail(ViewError(ErrorCode.GLOBAL_INTERNET_ERROR))
            BDDMockito.given(repository.getArticlesBySource(1)).willAnswer { throw IOException() }

            val actual = useCase.invoke(ArticleParam("10", 1))

            assertEquals(expected, actual)
        }
    }
}