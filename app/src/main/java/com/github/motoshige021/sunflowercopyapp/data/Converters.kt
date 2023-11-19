package com.github.motoshige021.sunflowercopyapp.data

import androidx.room.TypeConverter
import java.util.Calendar

class Converters {
    @TypeConverter fun calenderToDatestamp(calender: Calendar): Long = calender.timeInMillis
    @TypeConverter fun datestampToCalender(value: Long) : Calendar
        = Calendar.getInstance().apply { this.timeInMillis = value }
}