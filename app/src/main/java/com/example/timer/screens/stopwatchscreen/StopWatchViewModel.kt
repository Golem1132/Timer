package com.example.timer.screens.stopwatchscreen

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timer.service.TimerService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StopWatchViewModel: ViewModel() {

    private val _binder = MutableStateFlow<TimerService.MyBinder?>(null)
    val binder = _binder

    private val _timeTable = MutableStateFlow<List<Long>>(listOf())
    val timeTable = _timeTable.asStateFlow()

    fun addToList(newRecord: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _timeTable.emit(_timeTable.value.plus(newRecord))
        }
    }

    fun resetList() {
        _timeTable.value = listOf()
    }


    val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            _binder.value = p1 as TimerService.MyBinder
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            _binder.value = null
        }

    }
}