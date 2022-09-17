package com.example.newsource.data.remote.service

import com.example.core.data.remote.response.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsSourceService {

    @GET("v2/top-headlines?country=us")
    suspend fun getNewsSourceByCategory(
        @Query("category") categoryId: String,
        @Query("page") page: Int
    ): NewsResponse
}