package com.example.traceassistant.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyAppFragmentAdapter(activity: AppCompatActivity,val fragmentList: List<Fragment>): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int =fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}