package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {

    private val test = InitDatabase(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val atv01: TextView = findViewById(R.id.tv01)
        val abtn01: Button = findViewById(R.id.btn01)
        val abtn02: Button = findViewById(R.id.btn02)
        val alayout: ConstraintLayout = findViewById(R.id.layout)

        val listener = View.OnClickListener { view ->
            when (view.id) {
                R.id.btn01 -> startActivity(Intent(this@MainActivity, Me::class.java))
                R.id.btn02 -> {
                    val intent = Intent(this@MainActivity, LogIn::class.java)
                    startActivity(intent)
                }
            }
        }

        abtn01.setOnClickListener(listener)
        abtn02.setOnClickListener(listener)
    }
}
