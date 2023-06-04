package com.example.timer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.timer.data.Exercise
import com.example.timer.service.TimerService
import com.example.timer.ui.theme.TimerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel by viewModels<TimerViewModel>()
        startService(Intent(this, TimerService::class.java))
        bindService(
            Intent(this, TimerService::class.java),
            viewModel.connection,
            Context.BIND_AUTO_CREATE
        )
        var service: TimerService? = viewModel.binder.value?.getBoundService()
        viewModel.binder.observe(
            this
        ) { binder ->
            service = binder?.getBoundService()
        }


        setContent {
            TimerTheme {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Current time: ${0}")
                    Row(horizontalArrangement = Arrangement.spacedBy(50.dp)) {
                        Button(onClick = {
                            if (service?.currentExercise != null)
                                service?.resume()
                            else {
                                service?.currentExercise = Exercise(
                                    1,
                                    "XD",
                                    10000L
                                )
                                service?.start()
                            }
                        }) {
                            Text(text = "START")
                        }
                        Button(onClick = {
                            service?.pause()
                        }) {
                            Text(text = "Stop")
                        }
                    }
                }

            }
        }
    }
}