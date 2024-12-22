package com.alexP.socialnetwork.ui.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.alexP.socialnetwork.R

class MainActivity : AppCompatActivity(R.layout.activity_main){
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
    }
}