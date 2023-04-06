package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class hospital extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital);



//        底部tag栏事件

        findViewById(R.id.tagLayout1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(hospital.this,Main_doctor.class));
                System.out.println("第一个");
            }
        });
        findViewById(R.id.tagLayout2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(hospital.this,add_plan.class));
                System.out.println("第二个");
            }
        });
        findViewById(R.id.tagLayout3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(hospital.this,hospital.class));
                System.out.println("第三个");
            }
        });






    }
}
