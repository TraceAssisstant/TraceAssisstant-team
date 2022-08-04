package com.example.traceassistant.Tools

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat.requestPermissions
import com.example.traceassistant.R
import com.example.traceassistant.ui.main.MainView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * 快捷显示Toast提示的工具方法
 * 示例：“ABCD”.showToast()   R.String.app_theme.showToast(Toast.LENGTH_LONG)
 */
fun String.showToast(duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(GlobalApplication.context,this,duration).show()
}
fun Int.showToast(duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(GlobalApplication.context,this,duration).show()
}

/**
 * 将布尔转1或0
 */
fun Boolean.toInt() = if (this) 1 else 0

/**
 * 申请定位权限（仅前台gps精确定位权限）
 */
fun locationPermission(activity: Activity){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),100)
    }else{
        MaterialAlertDialogBuilder(activity,
        com.google.android.material.R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
            .setMessage("安卓版本号过低")
            .setPositiveButton("ok"){dialog,which ->
                val intent = Intent(activity, MainView::class.java)
                activity.startActivity(intent)
            }
    }
}