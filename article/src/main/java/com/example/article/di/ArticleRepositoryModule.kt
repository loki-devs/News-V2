package com.example.article.di

import com.example.article.data.repository.ArticleRepository
import com.example.article.domain.source.ArticleDataSource
import org.koin.dsl.module

val articleRepositoryModule = module {
    factory { ArticleRepository(get()) as ArticleDataSource }
}