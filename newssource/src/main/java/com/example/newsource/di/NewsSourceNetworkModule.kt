package com.example.newsource.di

import com.example.core.di.provideRetrofit
import com.example.newsource.data.remote.service.NewsSourceService
import org.koin.dsl.module

val newsSourceNetworkModule = module {
    factory { provideRetrofit<NewsSourceService>(get(), get()) }
}