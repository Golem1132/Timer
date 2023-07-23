package com.example.timer.screens.trainingcomposer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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

    fun addExercise(exercise: Exercise) {
        viewModelScope.launch {
            _exercisesList.emit(_exercisesList.value.plus(exercise))
        }
    }

    fun clearList() {
        viewModelScope.launch {
            _exercisesList.emit(emptyList())
        }
    }

}