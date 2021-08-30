package com.example.tah

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

interface LiveDataUtil {
    fun <T> LiveData<T>?.getOrAwait(): T? {
        var value: T? = null
        val latch = CountDownLatch(1)

        val observer = Observer<T> { t ->
            value = t
            latch.countDown()
        }

        GlobalScope.launch(Dispatchers.Main) {
            this@getOrAwait?.observeForever(observer)
        }

        latch.await(2, TimeUnit.SECONDS)
        return value
    }
}