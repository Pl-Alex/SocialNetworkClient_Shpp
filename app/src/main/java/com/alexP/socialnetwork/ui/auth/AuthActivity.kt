package com.alexP.socialnetwork.ui.auth

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.alexP.socialnetwork.R

class AuthActivity : AppCompatActivity(R.layout.activity_auth){
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
    }
}