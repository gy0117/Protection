package com.mars.infra.protection

import android.app.Application
import com.mars.infra.protection.lib.CrashKiller
import com.mars.infra.protection.lib.UncaughtExceptionConsumer
import java.lang.NullPointerException

/**
 * Created by Mars on 2022/4/14
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        CrashKiller.apply {
            init(this@App)
            addConsumer(object : UncaughtExceptionConsumer {
                override fun consume(t: Thread?, e: Throwable?): Boolean {
                    return e is NullPointerException
                }
            })
            start()
        }
    }
}