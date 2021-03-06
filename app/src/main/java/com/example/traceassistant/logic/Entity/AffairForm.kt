package com.example.traceassistant.logic.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.awt.font.TextAttribute

//事务实体类
@Entity(tableName = "affair_form")
data class AffairForm(
    @ColumnInfo(name = "title") val ttitle: String,
    @ColumnInfo(name = "main_content") val mainContent:String,
    @ColumnInfo(name = "time") val time:Long,
    @ColumnInfo(name = "longitude") val longitude:Double,
    @ColumnInfo(name = "latitude") val latitude:Double,
    @ColumnInfo(name = "range") val locRange:Double,
    @ColumnInfo(name = "repeat_type") val repeatType: Int,
    @ColumnInfo(name ="level" ) val level:Int,    //分五级,一级为最紧急重要
    @ColumnInfo(name = "tag") val tag:String,
    @ColumnInfo(name = "ring_music") val ringMusic:Boolean,
    @ColumnInfo(name = "is_shake") val isShake:Boolean,
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0



}