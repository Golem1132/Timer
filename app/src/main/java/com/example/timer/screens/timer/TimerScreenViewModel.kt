package com.example.timer.screens.timer

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import com.example.timer.service.TimerService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TimerScreenViewModel: ViewModel() {

    private val _binder = MutableStateFlow<TimerService.MyBinder?>(null)
    val binder = _binder.asStateFlow()

    val connection = object: ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            if (p1 != null) {
                _binder.value = p1 as TimerService.MyBinder
            }
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            _binder.value = null
        }
    }
}