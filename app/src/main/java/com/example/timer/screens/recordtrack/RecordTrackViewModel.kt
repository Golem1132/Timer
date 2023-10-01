package com.example.timer.screens.recordtrack

import android.content.ComponentName
import android.content.ServiceConnection
import android.location.Location
import android.os.IBinder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timer.internal.RecordTrackUiEvent
import com.example.timer.internal.TimerState
import com.example.timer.service.TimerService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RecordTrackViewModel : ViewModel() {

    private val _binder = MutableStateFlow<TimerService.MyBinder?>(null)
    val binder = _binder.asStateFlow()

    var currentZoom = 5.0
    private val _distance = MutableStateFlow<Float>(0f)
    val distance = _distance.asStateFlow()

    private val _location = TimerService.currentLocation
    val location = _location.asStateFlow()

    init {
        viewModelScope.launch {
            TimerService.currentLocation.collectLatest {
                if (_binder.value?.getStopWatchState()?.value == TimerState.RUNNING)
                    calculateDistance(it)
            }
        }
    }

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

    private fun calculateDistance(location: Location?) {
        _distance.value += location?.distanceTo(TimerService.prevLocation ?: location) ?: 0f
    }

    fun resetDistance() {
        _distance.value = 0f
    }

}
