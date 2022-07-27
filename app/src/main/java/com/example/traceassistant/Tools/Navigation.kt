package com.example.traceassistant.Tools

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.traceassistant.R
import com.example.traceassistant.ui.affairShow.ShowView
import com.example.traceassistant.ui.affairsCollection.CollectionView
import com.example.traceassistant.ui.habit.HabitView
import com.example.traceassistant.ui.main.MainView
import com.example.traceassistant.ui.setting.SettingView
import com.google.android.material.bottomnavigation.BottomNavigationView

object Navigation {

    /**
     * 初始化导航栏
     * @param id 设置当前选中的导航栏图标
     * @param context 上下文
     * @param bottomNavigationView 导航栏id
     */
    fun initialize(id: Int,context: Context,bottomNavigationView: BottomNavigationView){
        bottomNavigationView.selectedItemId = id
        bottomNavigationView.setOnItemSelectedListener { item->
            when(item.itemId){
                R.id.homePage -> {
                    val intent = Intent(context,MainView::class.java)
                    context.startActivity(intent)
                    true
                }
                R.id.addPage -> {
                    val intent = Intent(context,CollectionView::class.java)
                    context.startActivity(intent)
                    true
                }
                R.id.showPage -> {
                    val intent = Intent(context,ShowView::class.java)
                    context.startActivity(intent)
                    true
                }
                R.id.settingPage -> {
                    val intent = Intent(context,SettingView::class.java)
                    context.startActivity(intent)
                    true
                }
                R.id.habitPage -> {
                    val intent = Intent(context,HabitView::class.java)
                    context.startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}