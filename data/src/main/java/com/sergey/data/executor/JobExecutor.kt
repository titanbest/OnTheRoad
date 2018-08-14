package com.sergey.data.executor

import com.sergey.domain.executor.ThreadExecutor
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

import javax.inject.Inject

class JobExecutor
@Inject constructor() : ThreadExecutor {
    private val threadPoolExecutor: ThreadPoolExecutor

    init {
        threadPoolExecutor = ThreadPoolExecutor(3, 5, 10, TimeUnit.SECONDS,
                LinkedBlockingQueue<Runnable>(), JobThreadFactory())
    }

    override fun execute(runnable: Runnable) {
        this.threadPoolExecutor.execute(runnable)
    }

    private class JobThreadFactory : ThreadFactory {
        private var counter = 0

        override fun newThread(runnable: Runnable): Thread {
            return Thread(runnable, "android_" + counter++)
        }
    }
}