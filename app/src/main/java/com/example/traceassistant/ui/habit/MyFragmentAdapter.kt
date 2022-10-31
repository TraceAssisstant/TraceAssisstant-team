package com.example.traceassistant.ui.habit

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyFragmentAdapter(activity: AppCompatActivity,val fragments: List<Fragment>): FragmentStateAdapter(activity) {
    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int)= fragments[position]

}