package com.mars.infra.protection.lib

import android.content.Context
import com.mars.infra.protection.lib.internal.ToastBadTokenException

/**
 * Created by JohnnySwordMan on 2021/12/9
 */
object CrashKiller {

    private var mConsumeExceptionHandler: ConsumeExceptionHandler? = null

    private var initialized = false

    private val crashConsumerList = mutableListOf<UncaughtExceptionConsumer>()

    fun init(context: Context) {
        if (initialized) {
            throw Exception("CrashKiller has initialized.")
        }
        initialized = true
        registerCrashKillerIfNeed()
        addConsumerInner()
    }

    /**
     * SDK内部提供一些异常的处理方式
     */
    private fun addConsumerInner() {
        // test case
        val aConsumerInner = object : UncaughtExceptionConsumer {
            override fun consume(t: Thread?, e: Throwable?): Boolean {
                return true
            }
        }
        val toastBadTokenException = ToastBadTokenException()
        crashConsumerList.apply {
            add(aConsumerInner)
            add(toastBadTokenException)
        }
    }


    fun addConsumer(consumer: UncaughtExceptionConsumer?) {
        consumer?.let { crashConsumerList.add(it) }
    }


    fun addConsumer(consumers: List<UncaughtExceptionConsumer>?) {
        consumers?.let {
            crashConsumerList.addAll(it)
        }
    }

    fun start() {
        if (!initialized) {
            throw Exception("CrashKiller must be initialize.")
        }
        registerCrashKillerIfNeed()
        mConsumeExceptionHandler!!.addConsumer(crashConsumerList)
    }


    private fun registerCrashKillerIfNeed() {
        if (mConsumeExceptionHandler == null) {
            mConsumeExceptionHandler = ConsumeExceptionHandler()
            mConsumeExceptionHandler!!.register()
        }
    }
}