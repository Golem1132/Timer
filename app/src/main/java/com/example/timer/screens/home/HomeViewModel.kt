package com.example.timer.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.timer.database.TimerDatabase
import com.example.timer.model.Training
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/*class HomeViewModelFactory(private val database: TimerDatabase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java))
            return HomeViewModel(database) as T
        throw IllegalArgumentException("Wrong View Model")
    }
}*/

class HomeViewModel(/*private val database: TimerDatabase*/): ViewModel() {

    private val _trainings = MutableStateFlow<List<Training>>(emptyList())
    val training = _trainings.asStateFlow()

    init {

    }

}