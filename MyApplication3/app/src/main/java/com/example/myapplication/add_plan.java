package com.example.myapplication;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class add_plan extends AppCompatActivity {
        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.add_plan);


            InitDatabase initDatabase = new InitDatabase(this);

            EditText editText = findViewById(R.id.newPath);

            findViewById(R.id.upload).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SQLiteDatabase database = initDatabase.getWritableDatabase();
//                    database.execSQL("drop table if exists doctor_" + Main_doctor.accountDoctor);
                    database.execSQL("create table if not exists doctor_"+Main_doctor.accountDoctor+" (paths varchar(200))");
                    ArrayList<String> paths = new ArrayList<>();
                    Cursor cursor = database.query("doctor_" + Main_doctor.accountDoctor,null, null, null, null, null, null);
                    while (cursor.moveToNext()) {
                        if (cursor.getColumnIndex("paths")>=0){
                            @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex("paths"));
                            paths.add(path);
                        }
                    }
                    System.out.println(paths);

                    if (!paths.contains(String.valueOf(editText.getText()))){
                        database.execSQL("insert into doctor_"+Main_doctor.accountDoctor+" (paths) values ('" + String.valueOf(editText.getText()) + "')");
                        System.out.println("创建成功");
                    }else {
                        System.out.println("存在该路线");
                    }

                    while (cursor.moveToNext()) {
                        if (cursor.getColumnIndex("paths")>=0){
                            @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex("paths"));
                            paths.add(path);
                        }
                    }

                    System.out.println(paths);




                }
            });



        }
}
