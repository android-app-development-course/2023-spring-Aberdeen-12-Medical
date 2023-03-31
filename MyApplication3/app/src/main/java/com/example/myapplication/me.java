package com.example.myapplication;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class me extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me);

        ImageView userView = (ImageView) findViewById(R.id.userLogo);
        userView.setImageResource(R.drawable.user_logo);


    }
}
