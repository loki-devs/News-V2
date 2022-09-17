package com.example.core.data.remote.response

data class NewsResponse(
    val status: String,
    val totalResult: Int,
    val articles: List<Articles>
) {

    data class Articles(
        val source: Source,
        val author: String,
        val title: String,
        val description: String,
        val url: String,
        val urlToImage: String,
        val publishedAt: String,
        val content: String
    )

    data class Source(
        val id: String?,
        val name: String?
    )
}