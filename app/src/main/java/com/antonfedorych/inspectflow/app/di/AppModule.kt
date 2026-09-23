package com.antonfedorych.inspectflow.app.di

import com.antonfedorych.inspectflow.data.local.InspectionDAO
import com.antonfedorych.inspectflow.data.local.InspectionDatabase
import org.koin.dsl.module

private const val API_BASE_URL =
    "https://gist.githubusercontent.com/aruana-lumiform/383e1291df3e0cc1d49aeb14d45f5b94/raw/"

val appModule = module {
    single { InjectionHelper.provideRetrofitClient(API_BASE_URL) }
    single { InjectionHelper.provideAPIService(get()) }
    single { InjectionHelper.provideRoomDB(get()) }
    single { InjectionHelper.provideInspectionDAO(get()) }
}