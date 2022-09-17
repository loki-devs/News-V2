package com.example.article.domain.source

import com.example.core.data.remote.response.NewsResponse

interface ArticleDataSource {

    suspend fun getArticlesBySource(page: Int): NewsResponse
}