package com.antonfedorych.inspectflow.app.di

import android.app.Application
import com.antonfedorych.inspectflow.data.repositoryModule
import com.antonfedorych.inspectflow.domain.useCaseModule
import com.antonfedorych.inspectflow.ui.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

object InjectionHelper {

    fun init(application: Application) {
        startKoin {
            androidContext(application)
            modules(
                repositoryModule,
                useCaseModule,
                viewModelModule,
            )
        }
    }
}
