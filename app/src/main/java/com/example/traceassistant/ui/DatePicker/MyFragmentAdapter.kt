package com.example.traceassistant.ui.DatePicker

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.traceassistant.ui.DatePicker.DateFragment

class MyFragmentAdapter(activity: AppCompatActivity, private val fragments: MutableList<DateFragment>) : FragmentStateAdapter(activity) {
    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return this.fragments[position]
    }
}