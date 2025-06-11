package com.duczxje.concurrency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun CoroutineScope.launchIO(block: suspend CoroutineScope.() -> Unit) : Job {
    return launch(AppDispatchers.IO, block = block)
}

fun CoroutineScope.launchMain(block: suspend CoroutineScope.() -> Unit) : Job {
    return launch(AppDispatchers.MAIN, block = block)
}

suspend fun <T> switchIO(block: suspend CoroutineScope.() -> T) : T {
    return withContext(AppDispatchers.IO, block = block)
}

suspend fun <T> switchMain(block: suspend CoroutineScope.() -> T) : T {
    return withContext(AppDispatchers.MAIN, block = block)
}