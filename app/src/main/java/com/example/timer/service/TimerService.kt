package com.example.timer.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import com.example.timer.data.Exercise
import kotlinx.coroutines.flow.MutableStateFlow

private const val interval = 1000L

class TimerService : Service(), TimerMethods {

    inner class MyBinder : Binder() {
        fun getBoundService() = this@TimerService
    }

    var currentExercise: Exercise? = null
    var currentTime: MutableStateFlow<Long?> = MutableStateFlow(null)

    private val binder = MyBinder()
    var timer: CountDownTimer? = null

    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }


    override fun onBind(p0: Intent?): IBinder {
        return binder
    }


    override fun start() {
        currentTime.value = currentExercise?.duration ?: 0L
        timer = object : CountDownTimer(currentTime.value ?: 0L, interval) {
            override fun onTick(p0: Long) {
                this@TimerService.currentTime.value = p0
                println(currentTime.value)
            }

            override fun onFinish() {
                println("FINISH")
            }
        }
        timer?.start()
    }

    override fun stop() {
        timer?.cancel()
        timer = null
        currentExercise = null

    }

    override fun pause() {
        timer?.cancel()
        timer = null
    }

    override fun resume() {
        timer = object : CountDownTimer(currentTime.value ?: 0L, interval) {
            override fun onTick(p0: Long) {
                currentTime.value = p0
                println(currentTime.value)
            }

            override fun onFinish() {
                println("FINISH")
            }
        }
        timer?.start()
    }
}

