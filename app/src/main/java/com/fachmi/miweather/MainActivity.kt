package com.fachmi.miweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fachmi.miweather.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        populateDataFromApi()
    }

    private fun populateDataFromApi() {
        binding.apply {
            tvDateTime.text = "2021-09-01 12:00:00"
        }
    }
}