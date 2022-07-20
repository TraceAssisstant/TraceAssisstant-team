package com.example.traceassistant.ui.affairsCollection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.traceassistant.R
import com.example.traceassistant.databinding.ActivityCollectionViewBinding

class CollectionView : AppCompatActivity() {
    private lateinit var binding: ActivityCollectionViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollectionViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items = listOf("工作日（周一至周五）","双休日（周六周日）","每天")
        val adapter = ArrayAdapter(this,R.layout.list_item,items)
        binding.repeatType.setAdapter(adapter)
    }
}