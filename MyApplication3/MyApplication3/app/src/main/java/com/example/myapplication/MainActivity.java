package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView atv01=(TextView) findViewById(R.id.tv01);
        Button abtn01=(Button) findViewById(R.id.btn01);
        Button abtn02=(Button) findViewById(R.id.btn02);
        ConstraintLayout alayout=(ConstraintLayout) findViewById(R.id.layout);
        //增加监听器及方法
        Button.OnClickListener Listener =new Button.OnClickListener(){
//            @SuppressWarnings("deprecation")
            public void onClick(View view) {
                // TODO Auto-generated method stub
                switch(view.getId()){
                    case R.id.btn01:
                        alayout.setBackgroundColor(Color.parseColor("#FFFF33"));
                        break;
                    case R.id.btn02:
                        //调用布局，更改布局颜色。使用系统中的color工具类parseColor设置
                        alayout.setBackgroundColor(Color.parseColor("#FF0000"));
                        break;
                }
            }};
        //给按钮绑定监听器，必须绑定监听器，否则没有效果
        abtn01.setOnClickListener(Listener);
        abtn02.setOnClickListener(Listener);
    }

}