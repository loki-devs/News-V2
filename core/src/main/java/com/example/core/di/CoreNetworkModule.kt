package com.example.core.di

import com.example.core.BuildConfig
import com.example.core.Timeout
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val API_KEY = "154d8ddc3f3a4e0f82bdb1b42b2f1917"

val coreNetworkModule = module {
    single { GsonBuilder().setLenient().create() }

    single(named("logging")) {
        HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT).apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        } as Interceptor
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<Interceptor>(named("logging")))
            .addInterceptor { chain ->
                val request = chain.request().newBuilder().addHeader("X-Api-Key", API_KEY).build()
                chain.proceed(request)
            }
            .readTimeout(Timeout.GENERAL_TIMEOUT, TimeUnit.MILLISECONDS)
            .connectTimeout(Timeout.GENERAL_TIMEOUT, TimeUnit.MILLISECONDS)
            .build()
    }
}

inline fun <reified T> provideRetrofit(okHttpClient: OkHttpClient, gson: Gson, baseUrl: String): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    return retrofit.create(T::class.java)
}