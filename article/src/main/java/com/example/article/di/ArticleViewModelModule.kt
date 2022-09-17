package com.example.article.di

import com.example.article.presentation.listarticle.ListArticleViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val articleViewModelModule = module {
    viewModel { ListArticleViewModel(get()) }
}