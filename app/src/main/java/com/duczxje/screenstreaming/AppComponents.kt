package com.duczxje.screenstreaming

import com.duczxje.concurrency.AppCoroutineScope
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun createAppModule() = module {
    single<AppCoroutineScope> { androidContext() as AppCoroutineScope }
}