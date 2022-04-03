package com.gb.stopwatch.framework.di

import android.app.Application
import com.gb.stopwatch.framework.os.TimeSource
import com.gb.stopwatch.logic.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {

    private val koinModule = module {
        viewModel { MainViewModel(get()) }
        single { TimeSource() }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(koinModule)
        }
    }
}