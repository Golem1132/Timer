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
    val location = TimerService.currentLocation.collectAsState()
    val binder = viewModel.binder.collectAsState()
    val timerState = binder.value?.getCurrentState()?.collectAsState()
    val followLocation = rememberSaveable {
        mutableStateOf(false)
    }
    val map = remember {
        mutableStateOf<MapView?>(null)
    }
    var gpsLocationProvider: GpsMyLocationProvider? = null
    var locationOverlay = remember {
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
                    text = "10:00:00",
                    textAlign = TextAlign.Center
                )
            },
            actions = {
                Text(text = "100km")
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
                    minZoomLevel = 5.0
                    setMultiTouchControls(true)
                    zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
                    setTileSource(TileSourceFactory.MAPNIK)
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
                                15.0,
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
                        binder.value?.nextExercise()
                    },
                    onPause = {
                        binder.value?.pauseExercise()
                    },
                    onResume = {
                        binder.value?.resumeExercise()
                    },
                    onStart = {
                        binder.value?.startExercise()
                    },
                    onStop = {
                        binder.value?.stopExercise()
                    }
                )
            }
        }
    }
}