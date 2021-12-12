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
            if (canConsumeInner(t, e)) {
                // 只关心主线程
                if (t != null && t.name.equals("main") && Looper.myLooper() == Looper.getMainLooper()) {
                    while (true) {
                        try {
                            Looper.loop()
                        } catch (ex: Exception) {
                            if (!canConsumeInner(t, ex)) {
                                invokeDefaultHandler(t, ex)
                                break
                            }
                        }
                    }
                }
            } else {
                invokeDefaultHandler(t, e)
            }
        } catch (throwable: Throwable) {
            invokeDefaultHandler(t, throwable)
        }
    }

    // 调用系统默认的异常处理器
    private fun invokeDefaultHandler(thread: Thread?, throwable: Throwable?) {
        if (mDefaultHandler != null && mDefaultHandler != this) {
            mDefaultHandler!!.uncaughtException(thread, throwable)
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