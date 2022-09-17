package com.example.article.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.article.domain.model.ArticleData
import com.example.article.domain.model.ArticleParam
import com.example.article.domain.usecase.ArticleUseCase
import com.example.article.getTestObserver
import com.example.article.presentation.listarticle.ListArticleViewModel
import com.example.core.ErrorCode
import com.example.core.data.remote.response.NewsResponse
import com.example.core.domain.Either
import com.example.core.presentation.viewstate.PaginationViewState
import com.example.core.presentation.viewstate.ViewError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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

class ListArticleViewModelTest : KoinTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val viewModel by inject<ListArticleViewModel>()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var articleUseCase: ArticleUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        startKoin {
            modules(module {
                single { ListArticleViewModel(articleUseCase) }
            })
        }
    }

    @After
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun `on get article by source success`() {
        runTest {
            BDDMockito.given(articleUseCase.invoke(ArticleParam("10", 1))).willReturn(
                Either.Success(
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
            )
            val expected = listOf(
                PaginationViewState.Loading(),
                PaginationViewState.Success(
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
                    ), isLastPage = true
                )
            )
            val actual = viewModel.articleState.getTestObserver().observedValues
            viewModel.getArticleBySource("10")
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `on get article by source error`() {
        runTest {
            val error = ViewError(ErrorCode.GLOBAL_UNKNOWN_ERROR)
            BDDMockito.given(articleUseCase.invoke(ArticleParam("10", 1))).willReturn(Either.Fail(error))
            val expected = listOf<PaginationViewState<List<ArticleData>>>(
                PaginationViewState.Loading(),
                PaginationViewState.Error(error)
            )
            val actual = viewModel.articleState.getTestObserver().observedValues
            viewModel.getArticleBySource("10")
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `on get article by source empty`() {
        runTest {
            BDDMockito.given(articleUseCase.invoke(ArticleParam("10", 1)))
                .willReturn(Either.Success(listOf()))
            val expected = listOf<PaginationViewState<List<ArticleData>>>(
                PaginationViewState.Loading(),
                PaginationViewState.EmptyData()
            )
            val actual = viewModel.articleState.getTestObserver().observedValues
            viewModel.getArticleBySource("10")
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `on get article by source pagination error`() {
        runTest {
            val error = ViewError(ErrorCode.GLOBAL_UNKNOWN_ERROR)
            BDDMockito.given(articleUseCase.invoke(ArticleParam("10", 2))).willReturn(Either.Fail(error))
            val expected = listOf<PaginationViewState<List<ArticleData>>>(
                PaginationViewState.LoadMoreLoading(),
                PaginationViewState.PaginationError()
            )
            val actual = viewModel.articleState.getTestObserver().observedValues
            viewModel.page = 2
            viewModel.getArticleBySource("10")
            assertEquals(expected, actual)
        }
    }
}