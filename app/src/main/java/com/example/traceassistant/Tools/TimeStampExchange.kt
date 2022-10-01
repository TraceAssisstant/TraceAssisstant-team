package com.example.traceassistant.Tools

import android.widget.SimpleAdapter
import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间戳单位： S（秒）
 * 日期格式： yyyy-mm-dd
 * 准确日期格式： yyyy-MM-dd HH-mm-ss
 */

/**
 * 时间戳转化为日期
 */
fun toDate(stamp:Long):String{
    var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    return simpleDateFormat.format(Date(stamp*1000))
}

/**
 * 时间戳转化为当天时间，精确到秒
 */
fun toDateTime(stamp:Long):String{
    var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    return simpleDateFormat.format(Date(stamp*1000))
}


/**
 * 准确时间转化为时间戳
 */
fun toStamp(date:String): Long {
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date).time/1000
}

/**
 * 时间戳转化为当天时间，精确到分钟
 * 无日期
 */
fun toDateTimeObscure(stamp:Long):String{
    var simpleDateFormat = SimpleDateFormat("HH:mm")
    return simpleDateFormat.format(Date(stamp*1000))
}

