package com.example.timer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Exercises")
data class Exercise(
    @PrimaryKey
    val id: Int = 0,
    val name: String,
    val duration: Long,
    val isRest: Boolean,
    val parentId: Int
) {
    companion object {
        fun default(): Exercise {
            return Exercise(
                name = "",
                duration = 0L,
                isRest = false,
                parentId = 0
            )
        }
    }
}

