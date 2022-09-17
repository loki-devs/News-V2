package com.example.news

import android.app.Application
import com.example.core.di.coreNetworkModule
import com.example.newsource.di.newsSourceNetworkModule
import com.example.newsource.di.newsSourceRepositoryModule
import com.example.newsource.di.newsSourceUseCaseModule
import com.example.newsource.di.newsSourceViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NewsApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@NewsApplication)
            modules(
                arrayListOf(
                    coreNetworkModule, newsSourceNetworkModule, newsSourceRepositoryModule, newsSourceUseCaseModule, newsSourceViewModelModule
                )
            )
        }
    }
}