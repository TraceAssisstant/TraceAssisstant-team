package com.example.traceassistant.logic.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit")
data class Habit(
    @ColumnInfo(name = "tags") val tags :String,
    @ColumnInfo(name="begin_time") val beginTime : Long,
    @ColumnInfo(name="end_time") val endTime : Long,
    @ColumnInfo(name="break_times") val breakTimes : Long,

){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0

}
