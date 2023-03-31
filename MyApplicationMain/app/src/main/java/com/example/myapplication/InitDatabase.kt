package com.example.myapplication

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class InitDatabase(context: Context) : SQLiteOpenHelper(context, DBNAME, null, VERSION) {

    companion object {
        private const val VERSION = 1
        private const val DBNAME = "Users.db"  // 创建数据库名叫 Users
    }

    // 创建数据库 这两个函数在数据库被创造之初，的时候都会被调用
    override fun onCreate(db: SQLiteDatabase) {
        // 创建密码表 user
        db.execSQL("CREATE TABLE user (userAccont varchar(50), userPassword varchar(50))")
    }

    // 数据库版本更新
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS user")
        onCreate(db)
    }
}
