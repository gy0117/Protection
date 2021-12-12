package com.mars.crashkiller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import java.lang.IllegalArgumentException
import java.lang.NullPointerException

class MainActivity : AppCompatActivity() {

    private lateinit var mBtnStartNpe: Button
    private lateinit var mBtnStartNpe2: Button
    private lateinit var mBtnStartException3: Button
    private lateinit var mBtnCheckMainThread: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBtnStartNpe = findViewById(R.id.btn_start_npe_exception)
        mBtnStartNpe2 = findViewById(R.id.btn_start_npe_exception2)
        mBtnStartException3 = findViewById(R.id.btn_start_exception3)
        mBtnCheckMainThread = findViewById(R.id.btn_check_main_thread)

        mBtnStartNpe.setOnClickListener {
            Toast.makeText(this, "第一次触发NPE", Toast.LENGTH_SHORT).show()

            val text: String? = null
            println("第一次触发NPE，text length = ${text!!.length}")
        }

        mBtnStartNpe2.setOnClickListener {
            Toast.makeText(this, "第二次触发NPE", Toast.LENGTH_SHORT).show()

            val text: String? = null
            println("第二次触发NPE，text length = ${text!!.length}")
        }

        mBtnStartException3.setOnClickListener {
            Toast.makeText(this, "第一次触发IllegalArgumentException", Toast.LENGTH_SHORT).show()

            invokeIllegalArgumentException()
        }

        mBtnCheckMainThread.setOnClickListener {
            Toast.makeText(this, "主线程正常运行", Toast.LENGTH_SHORT).show()
            Log.e("CrashKiller", "主线程正常运行")
        }

        CrashKiller.addConsumer(object : UncaughtExceptionConsumer {

            override fun consume(t: Thread?, e: Throwable?): Boolean {
                return e is NullPointerException
            }
        })
    }

    private fun invokeIllegalArgumentException() {
        throw IllegalArgumentException("出现异常了")
    }
}