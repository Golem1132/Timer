package com.example.timer.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Binder
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Process
import com.example.timer.R
import com.example.timer.internal.TimerState
import com.example.timer.model.Exercise
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider


private const val INTERVAL = 1000L
const val STOP_SERVICE = "STOP_SERVICE"

class TimerService : Service(), TimerMethods, LocationListener {

    private val countdownTimerScope =
        CoroutineScope(CoroutineName("CountDownTimerScope") + Dispatchers.IO)

    private lateinit var serviceHandler: Handler
    private lateinit var serviceLooper: Looper
    private var startId = 0

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            //do nothing
        }
    }

    companion object {
        val currentLocation: MutableStateFlow<Location?> = MutableStateFlow(null)
        var prevLocation: Location? = null
    }


    inner class MyBinder : Binder() {
        fun getBoundService() = this@TimerService
        fun getCurrentState() = this@TimerService.currentState
        fun getCurrentTime() = this@TimerService.currentTime
        fun getCurrentExercise() = this@TimerService.currentExercise
        fun startExercise() = this@TimerService.startExercise()
        fun stopExercise() = this@TimerService.stopExercise()
        fun resumeExercise() = this@TimerService.resumeExercise()
        fun pauseExercise() = this@TimerService.pauseExercise()
        fun nextExercise() = this@TimerService.nextExercise()

         fun startStopWatch() = this@TimerService.startStopWatch()
         fun resetStopWatch() = this@TimerService.resetStopWatch()
         fun saveRecordStopWatch() = this@TimerService.saveRecordStopWatch()
         fun pauseStopWatch() = this@TimerService.pauseStopWatch()
        fun getStopWatchState() = this@TimerService.stopWatchState
        fun getStopWatchCurrentTime() = this@TimerService.currentTimeStopWatch
        fun getStopWatchTotalTime() = this@TimerService.totalTimeStopWatch

        fun getProvider() = this@TimerService.gpsLocationProvider
        fun getLocationManager() = this@TimerService.locationManager
        fun locationListener() = this@TimerService
        fun requestUpdates() = this@TimerService.requestUpdates()
        fun removeUpdates() = this@TimerService.removeUpdates()
    }

    private val currentExercise: MutableStateFlow<Exercise?> = MutableStateFlow(null)
    private var currentTime: MutableStateFlow<Long?> = MutableStateFlow(null)
    private val currentState: MutableStateFlow<TimerState> = MutableStateFlow(TimerState.IDLE)
    private var timerJob: Job = Job()

    private val totalTimeStopWatch: MutableStateFlow<Long> = MutableStateFlow(0L)
    private val currentTimeStopWatch: MutableStateFlow<Long> = MutableStateFlow(0L)
    private val stopWatchState: MutableStateFlow<TimerState> = MutableStateFlow(TimerState.IDLE)
    private var stopWatchJob: Job = Job()

    private var locationManager: LocationManager? = null
    private var provider = LocationManager.GPS_PROVIDER
    private var gpsLocationProvider: GpsMyLocationProvider? = null

    private val binder = MyBinder()

    private val notificationChannel = NotificationChannel(
        "TimerNotificationChannel",
        "XDD",
        NotificationManager.IMPORTANCE_DEFAULT
    )


    override fun onCreate() {
        super.onCreate()
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
        HandlerThread("TimerServiceHandler", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            serviceLooper = looper
            serviceHandler = ServiceHandler(serviceLooper)
            gpsLocationProvider = GpsMyLocationProvider(this@TimerService)
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == STOP_SERVICE) {
            stopForeground(Service.STOP_FOREGROUND_REMOVE)
            stopSelf()

        } else {
            this@TimerService.startId = startId
            serviceHandler.obtainMessage().also {
                it.arg1 = startId
                serviceHandler.sendMessage(it)
            }

            val notification = Notification.Builder(this, notificationChannel.id)
                .setContentText("XD")
                .setChannelId(notificationChannel.id)
                .setSmallIcon(R.drawable.timer_icon_foreground)
            startForeground(1000, notification.build())
        }
        return START_STICKY
    }


    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }


    override fun startExercise() {
        currentState.tryEmit(TimerState.RUNNING)
        currentExercise.tryEmit(Exercise(1, "XD", 120_000L, false, 0))
        currentTime.value = currentExercise.value?.duration ?: 120_000L
        val countDownTimer = flow<Long> {
            while (currentTime.value != null && currentTime.value!! > 0L) {
                delay(INTERVAL)
                emit(currentTime.value!! - INTERVAL)
            }
        }
        timerJob = countdownTimerScope.launch {
            countDownTimer.collect {
                currentTime.emit(it)
            }
        }

    }

    override fun stopExercise() {
        currentState.tryEmit(TimerState.IDLE)
        currentExercise.tryEmit(null)
        currentTime.tryEmit(null)
    }

    override fun pauseExercise() {
        currentState.tryEmit(TimerState.PAUSED)
        timerJob.cancel()

    }

    override fun resumeExercise() {
        currentState.tryEmit(TimerState.RUNNING)
        val countDownTimer = flow<Long> {
            while (currentTime.value != null && currentTime.value!! > 0L) {
                delay(INTERVAL)
                emit(currentTime.value!! - INTERVAL)
            }
        }
        timerJob = countdownTimerScope.launch {
            countDownTimer.collect {
                currentTime.value = it
            }
        }

    }

    override fun nextExercise() {
        TODO("Not yet implemented")
    }

    override fun startStopWatch() {
        stopWatchState.tryEmit(TimerState.RUNNING)
        val stopWatch = flow<Long> {
            while (stopWatchState.value == TimerState.RUNNING) {
                delay(INTERVAL)
                emit(INTERVAL)
            }
        }
        stopWatchJob = countdownTimerScope.launch {
            stopWatch.collect {
                println(it)
                currentTimeStopWatch.emit(currentTimeStopWatch.value.plus(it))
                totalTimeStopWatch.emit(totalTimeStopWatch.value.plus(it))
            }
        }
    }

    override fun resetStopWatch() {
        stopWatchState.tryEmit(TimerState.IDLE)
        countdownTimerScope.launch {
            currentTimeStopWatch.emit(0L)
            totalTimeStopWatch.emit(0L)
        }

    }

    override fun saveRecordStopWatch() {
        countdownTimerScope.launch {
            currentTimeStopWatch.emit(0L)
        }
    }

    override fun pauseStopWatch() {
        stopWatchState.tryEmit(TimerState.PAUSED)
    }


    override fun onLocationChanged(p0: Location) {
        prevLocation = currentLocation.value
        currentLocation.tryEmit(p0)
        println(p0)
    }

    private fun requestUpdates() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            locationManager?.requestLocationUpdates(
                provider,
                1000L,
                1f,
                this@TimerService
            )
    }

    private fun removeUpdates() {
        println(locationManager ?: "NULL")
        locationManager?.removeUpdates(this)
    }
}

