package com.example.timer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import com.example.timer.screens.TimerScreen
import com.example.timer.screens.TimerViewModel
import com.example.timer.service.TimerService
import com.example.timer.ui.theme.TimerTheme

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: TimerViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[TimerViewModel::class.java]
        setContent {
            val binder = viewModel.binder.collectAsState()
            TimerTheme {
                TimerScreen(binder.value)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startService(Intent(this, TimerService::class.java))
        bindService(
            Intent(this, TimerService::class.java),
            viewModel.connection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(viewModel.connection)
    }
}