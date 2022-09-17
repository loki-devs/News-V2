package com.example.newsource.domain.source

import com.example.core.data.remote.response.NewsResponse

interface NewsSourceDataSource {

    suspend fun getNewsSourceByCategory(categoryId: String, page: Int): NewsResponse
}