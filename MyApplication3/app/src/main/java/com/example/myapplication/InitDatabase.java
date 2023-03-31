package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class InitDatabase extends SQLiteOpenHelper {
    private static final int VERSION=1;
    private static final String DBNAME="Users.db";   //  创建数据库名叫 Users

    public InitDatabase(Context context){
        super(context,DBNAME,null,VERSION);
    }
    //创建数据库 这两个函数在数据库被创造之初，的时候都会被调用
    public void onCreate(SQLiteDatabase db){
        //创建密码表  user
        db.execSQL("create table user (userAccont varchar(50), userPassword varchar(50))");
    }
    //数据库版本更新
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
    {
        db.execSQL("drop table if exists user");
        onCreate(db);
    }


}
