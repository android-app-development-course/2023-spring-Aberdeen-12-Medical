package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBar = supportActionBar
        actionBar?.hide()

        val textView1 = findViewById<TextView>(R.id.introductionView1)
        val textView2 = findViewById<TextView>(R.id.introductionView2)
        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.cartoon)

        lottieAnimationView.animate().translationY(-1000f).setDuration(1000).setStartDelay(2000)
        textView1.animate().translationY(1000f).setDuration(1000).setStartDelay(2000)
        textView2.animate().translationY(1000f).setDuration(1000).setStartDelay(2000)

        val timer = Timer()
        val timerTask: TimerTask = object : TimerTask() {
            override fun run() {
                val bottomNavigationView: BottomNavigationView
                bottomNavigationView = findViewById(R.id.bottomNavigationView)

                runOnUiThread {
                    bottomNavigationView.visibility = View.VISIBLE
                }

                if (savedInstanceState == null) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment, Home()).commit()
                }

                bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
                    val fragment: Fragment = when (item.itemId) {
                        R.id.home_ -> Home()
                        R.id.communication_ -> MainCommunication()
                        R.id.me_ -> MainUser()
                        else -> return@OnNavigationItemSelectedListener false
                    }
                    if (fragment != null) {
                        supportFragmentManager.beginTransaction().replace(R.id.fragment, fragment).commit()
                        return@OnNavigationItemSelectedListener true
                    }
                    false
                })
            }
        }
        timer.schedule(timerTask, 3400)
    }
}
