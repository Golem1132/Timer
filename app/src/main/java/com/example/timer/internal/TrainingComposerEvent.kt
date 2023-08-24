package com.example.timer.internal

sealed class TrainingComposerEvent() {
    object StartScreen : TrainingComposerEvent()
    object PickExercisesScreen : TrainingComposerEvent()
    object PickTypeScreen: TrainingComposerEvent()
    object PickDurationScreen: TrainingComposerEvent()
}