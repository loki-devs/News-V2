package com.example.article.data.remote.service

import com.example.core.data.remote.response.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleService {

    @GET("v2/top-headlines?country=us")
    suspend fun getArticlesBySource(
        @Query("page") page: Int
    ): NewsResponse
}