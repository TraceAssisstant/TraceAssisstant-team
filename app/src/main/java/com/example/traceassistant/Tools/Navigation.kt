/**
 * 导航栏操作类
 */
package com.example.traceassistant.Tools

import androidx.viewpager2.widget.ViewPager2
import com.example.traceassistant.R
import com.google.android.material.bottomnavigation.BottomNavigationView

object Navigation {

    /**
     * 初始化导航栏
     * @param id 设置当前选中的导航栏图标
     * @param context 上下文
     * @param bottomNavigationView 导航栏id
     */
    fun initialize(id: Int,viewPager2: ViewPager2,bottomNavigationView: BottomNavigationView){
        bottomNavigationView.selectedItemId = id
        bottomNavigationView.setOnItemSelectedListener { item->
            when(item.itemId){
                R.id.homePage -> {
                    viewPager2.setCurrentItem(2,true)
                    true
                }
                R.id.addPage -> {
                    viewPager2.setCurrentItem(1,true)
                    true
                }
                R.id.showPage -> {
                    viewPager2.setCurrentItem(3,true)
                    true
                }
                R.id.settingPage -> {
                    viewPager2.setCurrentItem(4,true)
                    true
                }
                R.id.habitPage -> {
                    viewPager2.setCurrentItem(0,true)
                    true
                }
                else -> false
            }
        }
    }
}