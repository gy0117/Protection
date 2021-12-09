package com.mars.crashkiller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import java.lang.NullPointerException

class MainActivity : AppCompatActivity() {

    lateinit var mBtnStartNpe: Button
    lateinit var mBtn2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBtnStartNpe = findViewById(R.id.btn_start_npe_exception)
        mBtn2 = findViewById(R.id.btn_2)

        mBtnStartNpe.setOnClickListener {
            invokeNpeException()
        }

        mBtn2.setOnClickListener {
            Log.e("CrashKiller", "主线程正常运行")
        }

        CrashKiller.addConsumer(object : UncaughtExceptionConsumer {

            override fun consume(t: Thread?, e: Throwable?): Boolean {
                return e is NullPointerException
            }
        })
    }

    private fun invokeNpeException() {
        val text: String? = null
        println("text length = ${text!!.length}")
    }
}