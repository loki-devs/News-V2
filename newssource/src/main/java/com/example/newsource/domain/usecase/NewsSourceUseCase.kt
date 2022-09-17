package com.example.newsource.domain.usecase

import com.example.core.domain.UseCase
import com.example.newsource.domain.model.NewsSourceData
import com.example.newsource.domain.model.NewsSourceParam

interface NewsSourceUseCase: UseCase<NewsSourceParam, List<NewsSourceData>>