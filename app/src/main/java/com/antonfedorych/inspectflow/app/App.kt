package com.antonfedorych.inspectflow.app

import android.app.Application
import com.antonfedorych.inspectflow.app.di.InjectionHelper

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        InjectionHelper.init(this)
    }
}
