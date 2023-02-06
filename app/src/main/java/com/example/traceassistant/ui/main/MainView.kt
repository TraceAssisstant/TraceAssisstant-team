package com.example.traceassistant.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.amap.api.maps.MapsInitializer
import com.example.traceassistant.R
import com.example.traceassistant.Tools.LocalNowLocation
import com.example.traceassistant.Tools.Navigation
import com.example.traceassistant.Tools.locationPermission
import com.example.traceassistant.databinding.ActivityMainViewBinding
import com.example.traceassistant.service.AffairService
import com.example.traceassistant.service.GeoFenceService
import com.example.traceassistant.ui.MyAppFragmentAdapter
import com.example.traceassistant.ui.affairShow.ShowFragment
import com.example.traceassistant.ui.affairsCollection.CollectionFragment
import com.example.traceassistant.ui.affairsCollection.CollectionViewModel
import com.example.traceassistant.ui.habit.HabitFragment
import com.example.traceassistant.ui.setting.SettingFragment

class MainView : AppCompatActivity() {

    object MyActivityNow{
        var activity : AppCompatActivity? = null
    }

    private lateinit var binding: ActivityMainViewBinding

    val viewModel by lazy { ViewModelProvider(this).get(CollectionViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        MyActivityNow.activity = this
        super.onCreate(savedInstanceState)
        binding = ActivityMainViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * 定位权限申请
         */
        locationPermission(this)

        /**
         * 开启后台定时任务&事务提醒服务
         */
        val timeServiceIntent = Intent(this, AffairService::class.java)
        startService(timeServiceIntent)



        /**
         * 导航栏
         */
        Navigation.initialize(R.id.homePage,binding.fragmentRoot,binding.bottomNavigation)

        val listOfFragment = listOf<Fragment>(HabitFragment(),CollectionFragment(),MainFragment(),ShowFragment(),SettingFragment())
        val viewPager2Adapter = MyAppFragmentAdapter(this,listOfFragment)
        binding.fragmentRoot.adapter = viewPager2Adapter
        binding.fragmentRoot.setCurrentItem(2,false)
        binding.fragmentRoot.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                binding.bottomNavigation.selectedItemId = when(position){
                    0 -> R.id.habitPage
                    1 -> R.id.addPage
                    2 -> R.id.homePage
                    3 -> R.id.showPage
                    4 -> R.id.settingPage
                    else -> R.id.homePage
                }
                super.onPageSelected(position)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        /**
         * 初始化定位
         */
        MapsInitializer.updatePrivacyShow(this,true,true)
        MapsInitializer.updatePrivacyAgree(this,true)
        LocalNowLocation.initialize()
    }


}