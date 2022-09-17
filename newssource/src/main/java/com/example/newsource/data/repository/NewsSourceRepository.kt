package com.example.newsource.data.repository

import com.example.core.data.remote.response.NewsResponse
import com.example.newsource.data.remote.service.NewsSourceService
import com.example.newsource.domain.source.NewsSourceDataSource

class NewsSourceRepository(private val newsSourceService: NewsSourceService) : NewsSourceDataSource {

    override suspend fun getNewsSourceByCategory(
        categoryId: String,
        page: Int
    ): NewsResponse = newsSourceService.getNewsSourceByCategory(categoryId, page)

}