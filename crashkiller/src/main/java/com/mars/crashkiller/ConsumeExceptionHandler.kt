package com.mars.crashkiller

import android.os.Looper

/**
 * Created by JohnnySwordMan on 2021/12/9
 */
internal class ConsumeExceptionHandler : Thread.UncaughtExceptionHandler {

    private var hasRegister = false
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null
    private val consumerList = mutableListOf<UncaughtExceptionConsumer>()

    override fun uncaughtException(t: Thread?, e: Throwable?) {
        try {
            if (!canConsumeInner(t, e)) {
                mDefaultHandler?.uncaughtException(t, e)
            } else if (t?.name == "main") {
                // 保证主线程正常执行
                while (true) {
                    Looper.loop()
                }
            }
        } catch (ignore: Exception) {
            mDefaultHandler?.uncaughtException(t, e)
        }
    }

    /**
     * 是否可以内部消费
     */
    private fun canConsumeInner(t: Thread?, e: Throwable?): Boolean {
        try {
            synchronized(consumerList) {
                for (consumer in consumerList) {
                    if (consumer.consume(t, e)) {
                        return true
                    }
                }
            }
        } catch (e: Exception) {

        }
        return false
    }

    fun register() {
        if (!hasRegister) {
            mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
            if (mDefaultHandler != this) {
                Thread.setDefaultUncaughtExceptionHandler(this)
            }
            hasRegister = true
        }
    }

    @Synchronized
    fun addConsumer(consumer: UncaughtExceptionConsumer) {
        consumerList.add(consumer)
    }

    @Synchronized
    fun removeConsumer(consumer: UncaughtExceptionConsumer) {
        consumerList.remove(consumer)
    }

    @Synchronized
    fun clear() {
        consumerList.clear()
    }
}

interface UncaughtExceptionConsumer {

    @Throws(Throwable::class)
    fun consume(t: Thread?, e: Throwable?): Boolean
}