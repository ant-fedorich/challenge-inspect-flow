package com.antonfedorych.inspectflow.app.di

import android.app.Application
import com.antonfedorych.inspectflow.data.remote.ApiService
import com.antonfedorych.inspectflow.data.repositoryModule
import com.antonfedorych.inspectflow.domain.useCaseModule
import com.antonfedorych.inspectflow.ui.viewModelModule
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object InjectionHelper {

    fun init(application: Application) {
        startKoin {
            androidContext(application)
            modules(
                appModule,
                repositoryModule,
                useCaseModule,
                viewModelModule,
            )
        }
    }

    fun provideRetrofitClient(baseUrl: String): Retrofit {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val okHttpClient = OkHttpClient.Builder().build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    fun provideAPIService(retrofit: Retrofit): ApiService =
        retrofit.create<ApiService>()
}
