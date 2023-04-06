package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;


public class Main_user extends AppCompatActivity {

    InitDatabase test = new InitDatabase(this);

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        SQLiteDatabase database = test.getWritableDatabase();
//        test.onUpgrade(database, 1 ,1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_user);

        int[] imageViews = {R.id.imageViewmona, R.id.imageViewguizhong,
                R.id.imageViewmei, R.id.imageViewfengyuanwanye, R.id.imageViewabeidduo,R.id.imageViewnilu,R.id.imageViewnuoaier,R.id.imageViewheita};

        for (int imageID : imageViews) {
            findViewById(imageID).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(Main_user.this, hospital_content.class);
                        intent.putExtra("imageNameID",imageID);
                        System.out.println(v.getTag());
                        System.out.println(v.getId());
                        startActivity(intent);
                    }catch (Exception ignored){}
                    // 根据 id 处理点击事件
                }
            });
        }



//        底部tag栏事件


        findViewById(R.id.tagLayout1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main_user.this,hospital_content.class));
                System.out.println("第一个");
            }
        });
        findViewById(R.id.tagLayout2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main_user.this, Main_user.class));
                System.out.println("第二个");
            }
        });
        findViewById(R.id.tagLayout3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main_user.this,me.class));
                System.out.println("第三个");
            }
        });




    }
}