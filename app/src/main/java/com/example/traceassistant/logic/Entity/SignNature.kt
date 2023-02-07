/**
 * 图文实体类
 */
package com.example.traceassistant.logic.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

//图片文字数据类(id是当前日期(星期一 id就是1 ),从1 开始)
@Entity(tableName = "sign_nature",  indices = [Index(value = ["image_url"],unique = true)])
data class SignNature(
        @ColumnInfo(name = "text") val text:String,
        @ColumnInfo(name = "image_url") val imageUrl:Int

){
        @PrimaryKey(autoGenerate = true)
        var id:Int = 0

        override fun toString(): String {
                return "id(day): "+id+" "+super.toString();
        }
}
