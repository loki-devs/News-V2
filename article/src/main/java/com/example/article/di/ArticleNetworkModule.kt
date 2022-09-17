package com.example.article.di

import com.example.article.data.remote.service.ArticleService
import com.example.core.di.provideRetrofit
import org.koin.dsl.module

val articleNetworkModule = module {
    factory { provideRetrofit<ArticleService>(get(), get()) }
}