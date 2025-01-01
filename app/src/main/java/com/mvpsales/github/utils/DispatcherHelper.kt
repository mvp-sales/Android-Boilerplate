package com.mvpsales.github.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatcherHelper {

    fun ioDispatcher() : CoroutineDispatcher
    fun defaultDispatcher() : CoroutineDispatcher
}

class DispatcherHelperImpl : DispatcherHelper {
    override fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO
    override fun defaultDispatcher(): CoroutineDispatcher = Dispatchers.Main
}