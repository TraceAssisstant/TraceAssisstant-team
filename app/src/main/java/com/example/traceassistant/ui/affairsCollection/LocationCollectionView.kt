package com.example.traceassistant.ui.affairsCollection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.traceassistant.R
import com.example.traceassistant.databinding.ActivityLocationCollectionViewBinding

class LocationCollectionView : AppCompatActivity() {
    lateinit var binding: ActivityLocationCollectionViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationCollectionViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}