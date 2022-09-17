package com.example.newsource.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.article.domain.model.ArticleData
import com.example.core.ErrorCode
import com.example.core.domain.Either
import com.example.core.presentation.viewstate.PaginationViewState
import com.example.core.presentation.viewstate.ViewError
import com.example.newsource.domain.model.NewsSourceData
import com.example.newsource.domain.model.NewsSourceParam
import com.example.newsource.domain.usecase.NewsSourceUseCase
import com.example.newsource.getTestObserver
import com.example.newsource.presentation.NewsSourceViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.rules.TestRule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.BDDMockito
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class NewsSourceViewModelTest : KoinTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val viewModel by inject<NewsSourceViewModel>()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var newsSourceUseCase: NewsSourceUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        startKoin {
            modules(module {
                single { NewsSourceViewModel(newsSourceUseCase) }
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
            BDDMockito.given(newsSourceUseCase.invoke(NewsSourceParam("10", 1))).willReturn(
                Either.Success(
                    listOf(
                        NewsSourceData("1", "name")
                    )
                )
            )
            val expected = listOf(
                PaginationViewState.Loading(),
                PaginationViewState.Success(
                    listOf(
                        NewsSourceData("1", "name")), isLastPage = true
                )
            )
            val actual = viewModel.newsSourceState.getTestObserver().observedValues
            viewModel.getNewsSourceByCategory("10")
            Assert.assertEquals(expected, actual)
        }
    }

    @Test
    fun `on get news source by category error`() {
        runTest {
            val error = ViewError(ErrorCode.GLOBAL_UNKNOWN_ERROR)
            BDDMockito.given(newsSourceUseCase.invoke(NewsSourceParam("10", 1))).willReturn(Either.Fail(error))
            val expected = listOf<PaginationViewState<List<ArticleData>>>(
                PaginationViewState.Loading(),
                PaginationViewState.Error(error)
            )
            val actual = viewModel.newsSourceState.getTestObserver().observedValues
            viewModel.getNewsSourceByCategory("10")
            Assert.assertEquals(expected, actual)
        }
    }

    @Test
    fun `on get news source by category empty`() {
        runTest {
            BDDMockito.given(newsSourceUseCase.invoke(NewsSourceParam("10", 1)))
                .willReturn(Either.Success(listOf()))
            val expected = listOf<PaginationViewState<List<ArticleData>>>(
                PaginationViewState.Loading(),
                PaginationViewState.EmptyData()
            )
            val actual = viewModel.newsSourceState.getTestObserver().observedValues
            viewModel.getNewsSourceByCategory("10")
            Assert.assertEquals(expected, actual)
        }
    }

    @Test
    fun `on get news source by category pagination error`() {
        runTest {
            val error = ViewError(ErrorCode.GLOBAL_UNKNOWN_ERROR)
            BDDMockito.given(newsSourceUseCase.invoke(NewsSourceParam("10", 2))).willReturn(Either.Fail(error))
            val expected = listOf<PaginationViewState<List<ArticleData>>>(
                PaginationViewState.LoadMoreLoading(),
                PaginationViewState.PaginationError()
            )
            val actual = viewModel.newsSourceState.getTestObserver().observedValues
            viewModel.page = 2
            viewModel.getNewsSourceByCategory("10")
            Assert.assertEquals(expected, actual)
        }
    }
}