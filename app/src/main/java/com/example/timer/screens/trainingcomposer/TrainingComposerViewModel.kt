package com.example.timer.screens.trainingcomposer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.timer.internal.TrainingComposerEvent
import com.example.timer.model.Exercise
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TrainingComposerViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrainingComposerViewModel::class.java))
            return TrainingComposerViewModel() as T
        throw IllegalArgumentException("Wrong view model")
    }
}

class TrainingComposerViewModel : ViewModel() {
    private val _exercisesList = MutableStateFlow<List<Exercise>>(emptyList())
    val exercisesList = _exercisesList.asStateFlow()

    private val _uiEvent =
        MutableStateFlow<TrainingComposerEvent>(TrainingComposerEvent.StartScreen)
    val uiEvent = _uiEvent.asStateFlow()

    var trainingName = "Default name"
    var pickedTrainingIcon = MutableStateFlow(0)
    var parentId = 0

    var createdExercise: Exercise = Exercise.default()

    init {
//        TODO("Set up default training name")
        trainingName = ""
//        TODO("Set up parentId to the latest + 1")
        parentId = 0 + 1

    }

    fun sendUiEvent(uiEvent: TrainingComposerEvent) {
        viewModelScope.launch {
            _uiEvent.emit(uiEvent)
        }
    }

    fun addExercise() {
        viewModelScope.launch {
            _exercisesList.emit(_exercisesList.value.plus(createdExercise))
            createdExercise = Exercise.default()
        }
    }

    fun clearList() {
        viewModelScope.launch {
            _exercisesList.emit(emptyList())
        }
    }

}