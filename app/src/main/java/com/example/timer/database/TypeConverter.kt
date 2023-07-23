package com.example.timer.database

import java.sql.Date


class TypeConverter {
    @androidx.room.TypeConverter
    fun fromDateToString(date:Date): String = date.toString()
    @androidx.room.TypeConverter
    fun fromStringToDate(string: String): Date = Date.valueOf(string)
}