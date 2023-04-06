package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class me extends AppCompatActivity {
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me);

        ImageView userView = (ImageView) findViewById(R.id.userLogo);
        userView.setImageResource(R.drawable.user_logo);


//        底部tag栏事件

        findViewById(R.id.tagLayout1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(me.this,log_in.class));
                System.out.println("第一个");
            }
        });
        findViewById(R.id.tagLayout2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(me.this, Main_user.class));
                System.out.println("第二个");
            }
        });
        findViewById(R.id.tagLayout3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(me.this,me.class));
                System.out.println("第三个");
            }
        });



    }
}
