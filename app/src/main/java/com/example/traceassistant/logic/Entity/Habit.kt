/**
 * 习惯实体类
 */
package com.example.traceassistant.logic.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit")
data class Habit(
    @ColumnInfo(name = "title") var title :String,
    @ColumnInfo(name="begin_time") var beginTime : Long,
    @ColumnInfo(name="end_time") var endTime : Long,
    @ColumnInfo(name="pause_time") var pauseTime : Long,
    @ColumnInfo(name="date") var date :String

){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0

}
