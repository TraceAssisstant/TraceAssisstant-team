package com.example.traceassistant.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.amap.api.maps.MapsInitializer
import com.example.traceassistant.R
import com.example.traceassistant.Tools.*
import com.example.traceassistant.databinding.ActivityMainViewBinding
import com.example.traceassistant.logic.Repository
import com.example.traceassistant.service.AffairService
import com.example.traceassistant.service.GeoFenceService
import com.example.traceassistant.ui.MyAppFragmentAdapter
import com.example.traceassistant.ui.affairShow.ShowFragment
import com.example.traceassistant.ui.affairsCollection.CollectionFragment
import com.example.traceassistant.ui.affairsCollection.CollectionViewModel
import com.example.traceassistant.ui.habit.HabitFragment
import com.example.traceassistant.ui.setting.SettingFragment
import kotlin.concurrent.thread

class MainView : AppCompatActivity() {

    //地理服务获取成功
    var UPDATESERVICE = 1

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
        var handler: Handler

        handler = object :Handler(Looper.getMainLooper()){
            override fun handleMessage(msg: Message) {

                when(msg.what){
                    //检测到数据更新后更新服务数据
                    UPDATESERVICE -> {

                        /**
                         * 开启后台地理围栏&事务提醒服务
                         */
                        var GeoIntent : Intent = Intent()
                        GeoIntent.setClass(GlobalApplication.context, GeoFenceService::class.java)
                        startService(GeoIntent)
                        /**
                         * 开启后台定时任务&事务提醒服务
                         */
                        val timeServiceIntent = Intent()
                        timeServiceIntent.setClass(GlobalApplication.context, AffairService::class.java)
                        startService(timeServiceIntent)
                    }
                }
            }
        }
        //设置全局Handler
        ServiceHandler.setHandler(handler)

        thread{
            MapsInitializer.updatePrivacyShow(this, true, true)
            MapsInitializer.updatePrivacyAgree(this, true)
            //初始化地理位置信息
            LocalNowLocation.initialize()
            LocalNowLocation.startLocation()
            //每隔一秒检验一次地理服务状态

            Thread.sleep(6000)

            val msg = Message()
            //设置标志
            msg.what = UPDATESERVICE
            //向主线程通信
            ServiceHandler.getHandler().sendMessage(msg)
        }

        /**
         * 定位权限申请
         */
        locationPermission(this)

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