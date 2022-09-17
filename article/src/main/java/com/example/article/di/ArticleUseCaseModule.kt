package com.example.article.di

import com.example.article.domain.usecase.ArticleUseCase
import com.example.article.domain.usecase.ArticleUseCaseImpl
import org.koin.dsl.module

val articleUseCaseModule = module {
    factory { ArticleUseCaseImpl(get()) as ArticleUseCase }
}