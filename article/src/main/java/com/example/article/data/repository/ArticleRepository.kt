package com.example.article.data.repository

import com.example.article.data.remote.service.ArticleService
import com.example.article.domain.source.ArticleDataSource
import com.example.core.data.remote.response.NewsResponse

class ArticleRepository(private val articleService: ArticleService) : ArticleDataSource {

    override suspend fun getArticlesBySource(page: Int): NewsResponse =
        articleService.getArticlesBySource(page)
}