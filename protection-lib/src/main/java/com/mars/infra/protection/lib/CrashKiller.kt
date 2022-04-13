package com.mars.infra.protection.lib

/**
 * Created by JohnnySwordMan on 2021/12/9
 */
object CrashKiller {

    private var mConsumeExceptionHandler: ConsumeExceptionHandler? = null

    fun addConsumer(consumer: UncaughtExceptionConsumer) {
        registerCrashKillerIfNeed()
        mConsumeExceptionHandler!!.addConsumer(consumer)
    }

    fun removeConsumer(consumer: UncaughtExceptionConsumer) {
        registerCrashKillerIfNeed()
        mConsumeExceptionHandler!!.removeConsumer(consumer)
    }


    private fun registerCrashKillerIfNeed() {
        if (mConsumeExceptionHandler == null) {
            mConsumeExceptionHandler = ConsumeExceptionHandler()
            mConsumeExceptionHandler!!.register()
        }
    }
}