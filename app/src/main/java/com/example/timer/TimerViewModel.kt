package com.example.timer

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.timer.service.TimerService

class TimerViewModel : ViewModel() {
    val binder: MutableLiveData<TimerService.MyBinder?> by lazy {
        MutableLiveData<TimerService.MyBinder?>(null)
    }

    val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, iBinder: IBinder?) {
            binder.postValue(iBinder as TimerService.MyBinder)
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            binder.postValue(null)
        }
    }

}