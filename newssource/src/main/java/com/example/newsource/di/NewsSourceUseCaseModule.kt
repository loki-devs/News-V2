package com.example.newsource.di

import com.example.newsource.domain.usecase.NewsSourceUseCase
import com.example.newsource.domain.usecase.NewsSourceUseCaseImpl
import org.koin.dsl.module

val newsSourceUseCaseModule = module {
    factory { NewsSourceUseCaseImpl(get()) as NewsSourceUseCase }
}