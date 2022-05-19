package com.example.traceassistant.Tools

import android.widget.Toast

//快捷显示Toast提示的工具方法
//示例：“ABCD”.showToast()   R.String.app_theme.showToast(Toast.LENGTH_LONG)

fun String.showToast(duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(GlobalApplication.context,this,duration).show()
}
fun Int.showToast(duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(GlobalApplication.context,this,duration).show()
}

//