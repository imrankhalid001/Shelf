package com.shelf.personal.book

import android.app.Application
import com.shelf.personal.book.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ShelfApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ShelfApplication)
            modules(appModule)
        }
    }
}
