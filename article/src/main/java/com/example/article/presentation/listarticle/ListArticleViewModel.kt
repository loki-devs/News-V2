package com.example.article.presentation.listarticle

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.article.domain.model.ArticleData
import com.example.article.domain.model.ArticleParam
import com.example.article.domain.usecase.ArticleUseCase
import com.example.core.presentation.viewstate.PaginationViewState
import kotlinx.coroutines.launch

class ListArticleViewModel(private val articleUseCase: ArticleUseCase): ViewModel() {

    val articleState = MutableLiveData<PaginationViewState<ArticleData>>()

    private var page = 1

    private val limit = 20

    fun getArticleBySource(sourceId: String) {
        articleState.postValue(if (page > 1) PaginationViewState.LoadMoreLoading() else PaginationViewState.Loading())
        viewModelScope.launch {
            articleUseCase.invoke(ArticleParam(sourceId, page))
                .handleResult({
                    if (page == 1 && it.isEmpty()) {
                        articleState.postValue(PaginationViewState.EmptyData())
                    } else {
                        articleState.postValue(
                            PaginationViewState.Success(it, (it.isEmpty() || it.size < limit)))
                        page++
                    }
                }, {
                    if (page == 1) {
                        articleState.postValue(PaginationViewState.Error(it))
                    } else {
                        articleState.postValue(PaginationViewState.PaginationError())
                    }
                })
        }
    }

    fun resetPage() {
        page = 1
    }
}