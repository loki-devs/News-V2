package com.example.newsource.domain.usecase

import com.example.core.common.extension.asErrorObject
import com.example.core.data.remote.response.NewsResponse
import com.example.core.domain.Either
import com.example.core.presentation.viewstate.ViewError
import com.example.newsource.domain.model.NewsSourceData
import com.example.newsource.domain.model.NewsSourceParam
import com.example.newsource.domain.source.NewsSourceDataSource

class NewsSourceUseCaseImpl(private val newsSourceDataSource: NewsSourceDataSource) : NewsSourceUseCase {

    private var setSource: MutableSet<NewsSourceData> = HashSet()

    override suspend fun invoke(params: NewsSourceParam): Either<List<NewsSourceData>, ViewError> {
        return try {
            val result = newsSourceDataSource.getNewsSourceByCategory(params.categoryId, params.page)
            removeDuplicateList(result.articles)
            Either.Success(setSource.toTypedArray().asList().toList())
        } catch (e: Exception) {
            Either.Fail(e.asErrorObject())
        }
    }

    private fun removeDuplicateList(list: List<NewsResponse.Articles>) {
        for (i in list.indices) {
            setSource.add(NewsSourceData(list[i].source.id, list[i].source.name))
        }
    }
}