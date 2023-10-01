package com.example.timer.screens.recordtrack

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.timer.BuildConfig
import com.example.timer.R
import com.example.timer.components.CenterMapButton
import com.example.timer.components.TimerButtonsRow
import com.example.timer.service.TimerService
import com.example.timer.topappbar.TimerTopAppBar
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@Composable
fun MapScreen(navController: NavController) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val localContext = LocalContext.current
    val viewModel: RecordTrackViewModel = viewModel()
    val location = viewModel.location.collectAsState()
    val binder = viewModel.binder.collectAsState()
    val timerState = binder.value?.getStopWatchState()?.collectAsState()
    val totalTime = binder.value?.getStopWatchTotalTime()?.collectAsState()
    val distance = viewModel.distance.collectAsState()
    val followLocation = rememberSaveable {
        mutableStateOf(false)
    }
    val map = remember {
        mutableStateOf<MapView?>(null)
    }
    var gpsLocationProvider: GpsMyLocationProvider? = null
    val locationOverlay = remember {
        mutableStateOf<MyLocationNewOverlay?>(null)
    }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            binder.value?.requestUpdates()
        }
    }

    LaunchedEffect(key1 = binder.value, key2 = map.value) {
        binder.value.let {
            if (it != null) {
                it.requestUpdates()
                locationPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                gpsLocationProvider = GpsMyLocationProvider(binder.value!!.locationListener())
            } else {
                localContext.startForegroundService(Intent(localContext, TimerService::class.java))
                localContext.bindService(
                    Intent(localContext, TimerService::class.java),
                    viewModel.connection,
                    Context.BIND_AUTO_CREATE
                )
            }
        }
        map.value.let {
            if (it != null && binder.value != null) {
                locationOverlay.value = MyLocationNewOverlay(gpsLocationProvider, map.value)
                locationOverlay.value?.enableMyLocation()
                map.value!!.overlayManager.add(locationOverlay.value)
            }
        }
    }


    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                binder.value?.requestUpdates()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }



    Scaffold(topBar = {
        TimerTopAppBar(
            title = {
                Text(
                    text = buildAnnotatedString {
                        val seconds = (totalTime?.value ?: 0) / 1000
                        val minutes = seconds / 60
                        val hours = minutes / 60
                        val realSeconds = with(seconds) {
                            if (this > 59L)
                                this - (minutes * 60L)
                            else
                                this
                        }
                        append(
                            "${
                                if (hours < 10L)
                                    "0$hours"
                                else
                                    hours
                            }:${
                                if (minutes < 10L)
                                    "0$minutes"
                                else
                                    minutes
                            }:${
                                if (realSeconds < 10L)
                                    "0$realSeconds"
                                else
                                    realSeconds
                            }"
                        )
                    },
                    textAlign = TextAlign.Center
                )
            },
            actions = {
                Text(text = "${distance.value} km")
            },
            navIcon = {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Go back",
                    modifier = Modifier.clickable {
                        binder.value?.removeUpdates()
                        binder.value?.let {
                            it.getBoundService().stopForeground(Service.STOP_FOREGROUND_REMOVE)
                            it.getBoundService().stopSelf()
                        }
                        localContext.unbindService(viewModel.connection)
                        navController.popBackStack()
                    })
            }
        )
    }
    ) { paddingValues ->
        AndroidView(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
            factory = { ctx ->
                Configuration.getInstance()
                    .load(ctx, ctx.getSharedPreferences("MapPrefs", Context.MODE_PRIVATE))
                Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
                MapView(ctx).apply {
                    minZoomLevel = viewModel.currentZoom
                    setMultiTouchControls(true)
                    zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
                    setTileSource(TileSourceFactory.MAPNIK)
                    addMapListener(
                        object : MapListener {
                            override fun onScroll(event: ScrollEvent?): Boolean {
                                followLocation.value = locationOverlay.value?.isFollowLocationEnabled == true
                                return true
                            }

                            override fun onZoom(event: ZoomEvent?): Boolean {
                                viewModel.currentZoom = event?.zoomLevel ?: 5.0
                                return true
                            }

                        }
                    )
                }
            },
            update = {
                map.value = it
            })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.End
            ) {
                CenterMapButton(content = {
                    Image(
                        painter = painterResource(
                            id =
                            if (followLocation.value)
                                R.drawable.center_locked_icon
                            else
                                R.drawable.center_icon
                        ),
                        contentDescription = "Center to location"
                    )
                },
                    onClick = {
                        if (location.value != null) {
                            map.value?.controller?.animateTo(
                                GeoPoint(location.value!!.latitude, location.value!!.longitude),
                                viewModel.currentZoom,
                                500
                            )
                        }
                    },
                    onLongClick = {
                        followLocation.value = !followLocation.value
                        if (followLocation.value)
                            locationOverlay.value?.enableFollowLocation()
                        else locationOverlay.value?.disableFollowLocation()
                    })
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TimerButtonsRow(
                    timerState = timerState?.value,
                    onNext = {
                        binder.value?.saveRecordStopWatch()
                    },
                    onPause = {
                        binder.value?.pauseStopWatch()
                    },
                    onResume = {
                        binder.value?.startStopWatch()
                    },
                    onStart = {
                        binder.value?.startStopWatch()
                    },
                    onStop = {
                        binder.value?.resetStopWatch()
                        viewModel.resetDistance()
                    },
                    shouldShowNext = false
                )
            }
        }
    }
}