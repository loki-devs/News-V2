package com.example.article.domain.model

import com.example.core.data.remote.response.NewsResponse

data class ArticleData(
    val source: NewsResponse.Source,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String
)