package com.mars.infra.protection.lib

/**
 * Created by Mars on 2022/4/14
 */
interface UncaughtExceptionConsumer {

    @Throws(Throwable::class)
    fun consume(t: Thread?, e: Throwable?): Boolean
}