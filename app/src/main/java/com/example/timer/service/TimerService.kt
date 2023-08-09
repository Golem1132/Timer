package com.example.timer.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import com.example.timer.model.Exercise
import com.example.timer.internal.TimerState
import kotlinx.coroutines.flow.MutableStateFlow

private const val interval = 1000L

class TimerService : Service(), TimerMethods {

    inner class MyBinder : Binder() {
        fun getBoundService() = this@TimerService
        fun getCurrentState() = this@TimerService.currentState
        fun getCurrentTime() = this@TimerService.currentTime
        fun getCurrentExercise() = this@TimerService.currentExercise
        fun start() = this@TimerService.start()
        fun stop() = this@TimerService.stop()
        fun resume() = this@TimerService.resume()
        fun pause() = this@TimerService.pause()
        fun next() = this@TimerService.next()
    }

    private val currentExercise: MutableStateFlow<Exercise?> = MutableStateFlow(null)
    private var currentTime: MutableStateFlow<Long?> = MutableStateFlow(null)
    private val currentState: MutableStateFlow<TimerState> = MutableStateFlow(TimerState.IDLE)

    private val binder = MyBinder()
    var timer: CountDownTimer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }


    override fun onBind(p0: Intent?): IBinder {
        return binder
    }


    override fun start() {
        currentState.tryEmit(TimerState.RUNNING)
        currentTime.value = currentExercise.value?.duration ?: 120_000L
        currentExercise.tryEmit(Exercise(1, "XD", 120_000L,false, 0))
        timer = object : CountDownTimer(currentTime.value ?: 0L, interval) {
            override fun onTick(p0: Long) {
                this@TimerService.currentTime.value = p0
            }

            override fun onFinish() {
                currentState.tryEmit(TimerState.IDLE)
            }
        }
        timer?.start()
    }

    override fun stop() {
        currentState.tryEmit(TimerState.IDLE)
        timer?.cancel()
        timer = null
        currentExercise.tryEmit(null)
        currentTime.tryEmit(null)
    }

    override fun pause() {
        currentState.tryEmit(TimerState.PAUSED)
        timer?.cancel()
        timer = null
    }

    override fun resume() {
        currentState.tryEmit(TimerState.RUNNING)
        timer = object : CountDownTimer(currentTime.value ?: 0L, interval) {
            override fun onTick(p0: Long) {
                this@TimerService.currentTime.value = p0
            }

            override fun onFinish() {
                currentState.tryEmit(TimerState.IDLE)
            }
        }
        timer?.start()
    }

    override fun next() {
        TODO("Not yet implemented")
    }
}

