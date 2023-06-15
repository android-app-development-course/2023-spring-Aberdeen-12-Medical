package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment

class MainUser : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.activity_main_user, container, false)

        val userView: ImageView = view.findViewById(R.id.userLogo)
        userView.setImageResource(R.drawable.user_logo)
        val activity: Activity? = activity

        val constraintLayout: ConstraintLayout = view.findViewById(R.id.exit_layout)
        constraintLayout.isClickable = true
        constraintLayout.setOnClickListener {
            startActivity(Intent(activity, LogIn::class.java))
        }

        return view
    }
}
