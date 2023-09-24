package com.example.timer.service

interface TimerMethods {
    fun startExercise()
    fun stopExercise()
    fun pauseExercise()
    fun resumeExercise()
    fun nextExercise()

    fun startStopWatch()
    fun resetStopWatch()
    fun saveRecordStopWatch()
    fun pauseStopWatch()
}