package com.mars.infra.protection.lib.internal

import android.os.Build
import android.view.WindowManager
import com.mars.infra.protection.lib.UncaughtExceptionConsumer

/**
 * Created by Mars on 2022/4/20
 *
 * fix：Unable to add window -- token android.os.BinderProxy@2f61668 is not valid; is your activity running?
 */
class ToastBadTokenException: UncaughtExceptionConsumer {

    override fun consume(t: Thread?, e: Throwable?): Boolean {
        try {
            // 只有7.0才有，Android 8.0 系统已经修复
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N
                || Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                if (e != null && e is WindowManager.BadTokenException) {
                    for (element in e.stackTrace) {
                        if (element != null
                            && STACK_CLASS == element.className
                            && STACK_METHOD == element.methodName) {
                            return true
                        }
                    }
                }
            }
        } catch (ignore: Exception) {

        }
        return false
    }

    companion object {
        private const val STACK_CLASS = "android.widget.Toast\$TN"
        private const val STACK_METHOD = "handleShow"
    }
}