package com.example.newsource.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.presentation.viewstate.PaginationViewState
import com.example.newsource.domain.model.NewsSourceData
import com.example.newsource.domain.model.NewsSourceParam
import com.example.newsource.domain.usecase.NewsSourceUseCase
import kotlinx.coroutines.launch

class NewsSourceViewModel(private val newsSourceUseCase: NewsSourceUseCase) : ViewModel() {

    val newsSourceState = MutableLiveData<PaginationViewState<NewsSourceData>>()

    var page = 1

    private val limit = 20

    fun getNewsSourceByCategory(categoryId: String) {
        newsSourceState.postValue(if (page > 1) PaginationViewState.LoadMoreLoading() else PaginationViewState.Loading())
        viewModelScope.launch {
            newsSourceUseCase.invoke(NewsSourceParam(categoryId, page))
                .handleResult({
                    if (page == 1 && it.isEmpty()) {
                        newsSourceState.postValue(PaginationViewState.EmptyData())
                    } else {
                        newsSourceState.postValue(
                            PaginationViewState.Success(it, (it.isEmpty() || it.size < limit)))
                        page++
                    }
                }, {
                    if (page == 1) {
                        newsSourceState.postValue(PaginationViewState.Error(it))
                    } else {
                        newsSourceState.postValue(PaginationViewState.PaginationError())
                    }
                })
        }
    }

    fun resetPage() {
        page = 1
    }
}