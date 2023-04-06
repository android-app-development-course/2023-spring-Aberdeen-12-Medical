package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;

public class log_in extends AppCompatActivity {


    public static String getSha256(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        String temp;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuilder.append("0");
            }
            stringBuilder.append(temp);
        }
        return stringBuilder.toString();
    }

    InitDatabase initDatabase = new InitDatabase(this);
    private Boolean check(String identity, String account, String password){
        ArrayList<String> userData = new ArrayList<>();
        SQLiteDatabase database = initDatabase.getReadableDatabase();  // 这是一个对数据库操作的对象
        Cursor cursor = database.query(identity,null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            if (Objects.equals(identity, "user")){
                if (cursor.getColumnIndex("userAccount")>=0 && cursor.getColumnIndex("userPassword")>=0){
                    @SuppressLint("Range") String userAccount = cursor.getString(cursor.getColumnIndex("userAccount"));
                    @SuppressLint("Range") String userPassword = cursor.getString(cursor.getColumnIndex("userPassword"));
                    userData.add(userAccount + userPassword);
                }
            }else if (Objects.equals(identity, "doctor")){
                if (cursor.getColumnIndex("doctorAccount")>=0 && cursor.getColumnIndex("doctorPassword")>=0){
                    @SuppressLint("Range") String userAccount = cursor.getString(cursor.getColumnIndex("doctorAccount"));
                    @SuppressLint("Range") String userPassword = cursor.getString(cursor.getColumnIndex("doctorPassword"));
                    userData.add(userAccount + userPassword);
                }
            }
        }

        cursor.close();
        database.close();

        return userData.contains(account + getSha256(password));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        final CheckBox isDoctor = findViewById(R.id.isDoctor_log_in);


        findViewById(R.id.log_in).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                EditText account = findViewById(R.id.account_log_in);
                EditText password = findViewById(R.id.password_log_in);
                if (isDoctor.isChecked()){
                    if (check("doctor" ,String.valueOf(account.getText()),String.valueOf(password.getText()))) {
                        Intent intent = new Intent(log_in.this, Main_doctor.class);
                        intent.putExtra("account",String.valueOf(account.getText()));
                        startActivity(intent);
                    }else {
                        System.out.println("医生密码错误");
                    }
                }else {
                    if (check("user" ,String.valueOf(account.getText()),String.valueOf(password.getText()))) {
                        Intent intent = new Intent(log_in.this,Main_user.class);
                        intent.putExtra("account",String.valueOf(account.getText()));
                        startActivity(intent);
                    }else {
                        System.out.println("密码错误");
                    }
                }
            }
        });



        findViewById(R.id.textView_turn_signIn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(log_in.this,sign_in.class);
                startActivity(intent);
            }
        });




    }
}