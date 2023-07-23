package com.example.timer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.timer.model.Exercise
import com.example.timer.model.Training

@Database(
    version = 1,
    entities = [
        Training::class,
        Exercise::class
    ],
    exportSchema = true,
)
@TypeConverters(TypeConverter::class)
abstract class TimerDatabase : RoomDatabase() {


    private lateinit var INSTANCE: TimerDatabase

    fun getDatabase(context: Context): TimerDatabase {
        synchronized(TimerDatabase::class.java) {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    TimerDatabase::class.java,
                    "TimerDatabase"
                ).build()
            }
        }
        return INSTANCE
    }
}