package com.example.newsource.di

import com.example.newsource.presentation.NewsSourceViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val newsSourceViewModelModule = module {
    viewModel { NewsSourceViewModel(get()) }
}