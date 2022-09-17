package com.example.newsource.di

import com.example.newsource.data.repository.NewsSourceRepository
import com.example.newsource.domain.source.NewsSourceDataSource
import org.koin.dsl.module

val newsSourceRepositoryModule = module {
    factory { NewsSourceRepository(get()) as NewsSourceDataSource }
}