package com.example.article.domain.usecase

import com.example.article.domain.model.ArticleData
import com.example.article.domain.model.ArticleParam
import com.example.article.domain.source.ArticleDataSource
import com.example.core.common.extension.asErrorObject
import com.example.core.data.remote.response.NewsResponse
import com.example.core.domain.Either
import com.example.core.presentation.viewstate.ViewError

class ArticleUseCaseImpl(private val articleDataSource: ArticleDataSource) : ArticleUseCase {

    private var setSource: MutableSet<ArticleData> = HashSet()

    private var newsBySource: MutableList<NewsResponse.Articles> = ArrayList()

    override suspend fun invoke(params: ArticleParam): Either<List<ArticleData>, ViewError> {
        return try {
            val result = articleDataSource.getArticlesBySource(params.page)
            removeDuplicateList(result.articles.filter { it.source.name.equals(params.sourceName) })
            Either.Success(setSource.toTypedArray().asList().toList())
        } catch (e: Exception) {
            Either.Fail(e.asErrorObject())
        }
    }

    private fun removeDuplicateList(list: List<NewsResponse.Articles>) {
        for (i in list.indices) {
            with(list[i]) {
                setSource.add(
                    ArticleData(
                        source,
                        author,
                        title,
                        description,
                        urlToImage,
                        urlToImage,
                        publishedAt,
                        content
                    )
                )
            }
        }
    }
}