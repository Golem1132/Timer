package com.example.timer.screens.recordtrack

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timer.internal.RecordTrackUiEvent
import com.example.timer.service.TimerService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecordTrackViewModel : ViewModel() {

    private val _binder = MutableStateFlow<TimerService.MyBinder?>(null)
    val binder = _binder.asStateFlow()
    var currentZoom = 5.0

    val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            if (p1 != null)
                viewModelScope.launch {
                    _binder.emit(p1 as TimerService.MyBinder)
                }

        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            viewModelScope.launch {
                _binder.emit(null)
            }
        }

    }

    private val _uiEvent = MutableStateFlow<RecordTrackUiEvent>(RecordTrackUiEvent.PickTypeScreen)
    val uiEvent = _uiEvent.asStateFlow()

    fun sentUiEvent(uiEvent: RecordTrackUiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(uiEvent)
        }
    }

}
