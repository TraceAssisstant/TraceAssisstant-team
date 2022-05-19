package com.example.traceassistant.logic.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//图片文字数据类(id是当前日期(星期一 id就是1 ),从1 开始)
@Entity
data class SignNature(val text:String, val imageUrl:String){
        @PrimaryKey(autoGenerate = true)    //自动生成主键id
        var id:Int = 0;
        override fun toString(): String {
                return "id(day): "+id+" "+super.toString()
        }
}