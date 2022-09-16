package com.example.core.presentation.viewstate


sealed class ViewState<T> {
    data class Loading<T>(var progress: Float? = null) : ViewState<T>()
    data class Success<T>(var data: T) : ViewState<T>()
    data class Error<T>(var viewError: ViewError? = null) : ViewState<T>()
    data class SnackBarError<T>(var viewError: ViewError? = null): ViewState<T>()
}