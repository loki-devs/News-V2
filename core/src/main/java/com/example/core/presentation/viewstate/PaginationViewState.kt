package com.example.core.presentation.viewstate


sealed class PaginationViewState<T> {
    data class Loading<T>(var progress: Float? = null) : PaginationViewState<T>()
    data class LoadMoreLoading<T>(var isLoading: Float? = null) : PaginationViewState<T>()
    data class Success<T>(var data: List<T>, var isLastPage: Boolean = false) : PaginationViewState<T>()
    data class Error<T>(var viewError: ViewError? = null) : PaginationViewState<T>()
    data class PaginationError<T>(var viewError: ViewError? = null) : PaginationViewState<T>()
    data class EmptyData<T>(val unit: Unit = Unit) : PaginationViewState<T>()
}