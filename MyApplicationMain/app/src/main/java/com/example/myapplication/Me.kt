package com.example.myapplication

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Me : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.me)

        val userView = findViewById<ImageView>(R.id.userLogo)
        userView.setImageResource(R.drawable.user_logo)
    }
}
