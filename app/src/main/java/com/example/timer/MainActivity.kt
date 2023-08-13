package com.example.timer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import com.example.timer.navigation.TimerNavigation
import com.example.timer.screens.timer.MainViewModel
import com.example.timer.service.TimerService
import com.example.timer.ui.theme.TimerTheme

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setContent {
            val binder = viewModel.binder.collectAsState()
            TimerTheme {
                TimerNavigation(service = binder.value)
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