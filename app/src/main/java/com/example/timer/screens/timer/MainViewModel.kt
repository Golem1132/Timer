package com.example.timer.screens.timer

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import com.example.timer.service.TimerService
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : ViewModel() {
    private val _binder: MutableStateFlow<TimerService.MyBinder?> = MutableStateFlow(null)
    val binder = _binder


    val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, iBinder: IBinder?) {
            _binder.tryEmit(iBinder as TimerService.MyBinder)
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            _binder.tryEmit(null)
        }
    }

}