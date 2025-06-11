package com.duczxje.screenstreaming

import android.app.Application
import com.duczxje.appdependencies.createCoreModule
import com.duczxje.appdependencies.createDomainModule
import com.duczxje.concurrency.AppCoroutineScope
import com.duczxje.concurrency.AppDispatchers
import com.duczxje.screenstreaming.flutter.engine.mainFlutterEngine
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import kotlin.coroutines.CoroutineContext

class MainApplication : Application(), AppCoroutineScope {
    private lateinit var flutterEngine: FlutterEngine

    init {
        setupAppDispatchers()
    }

    override fun onCreate() {
        super.onCreate()
        setupKoin()
        setupFlutterEngine()
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@MainApplication)
            modules(
                createAppModule(),
                createCoreModule(),
                createDomainModule()
            )
        }
    }

    private fun setupAppDispatchers() {
        AppDispatchers.initialize(
            ioDispatcher = Dispatchers.IO,
            mainDispatcher = Dispatchers.Main
        )
    }

    private fun setupFlutterEngine() {
        flutterEngine = FlutterEngine(this)
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )
        FlutterEngineCache
            .getInstance()
            .put(mainFlutterEngine, flutterEngine)
    }

    override val coroutineContext: CoroutineContext by lazy {
        AppDispatchers.MAIN + SupervisorJob()
    }
}