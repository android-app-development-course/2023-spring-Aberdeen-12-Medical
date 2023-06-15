package com.example.myapplication

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class InitDatabase(context: Context) : SQLiteOpenHelper(context, DBNAME, null, VERSION) {

    companion object {
        private const val VERSION = 1
        private const val DBNAME = "Users.db" // 创建数据库名叫 Users
    }

    override fun onCreate(db: SQLiteDatabase) {
        //创建密码表  user
        db.execSQL("create table if not exists user (userAccount VARCHAR(50), userPassword VARCHAR(50))")
        db.execSQL("create table if not exists doctor (doctorAccount VARCHAR(50), doctorPassword VARCHAR(50), Title VARCHAR(30), Address VARCHAR(30), Phone VARCHAR(30), Official VARCHAR(30), Photo BLOB)")
        db.execSQL("create table if not exists images (photoName VARCHAR(50), source BLOB)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("drop table if exists user")
        db.execSQL("drop table if exists doctor")
        db.execSQL("drop table if exists images")
        onCreate(db)
    }
}
