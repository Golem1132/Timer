package com.example.timer.screens.stopwatchscreen

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import com.example.timer.service.TimerService
import kotlinx.coroutines.flow.MutableStateFlow

class StopWatchViewModel: ViewModel() {

    private val _binder = MutableStateFlow<TimerService.MyBinder?>(null)
    val binder = _binder

    val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            _binder.value = p1 as TimerService.MyBinder
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            _binder.value = null
        }

    }
}