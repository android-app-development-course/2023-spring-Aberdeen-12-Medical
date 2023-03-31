package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LogIn : AppCompatActivity() {

    companion object {
        fun getSha256(str: String): String {
            var messageDigest: MessageDigest
            var encodeStr = ""
            try {
                messageDigest = MessageDigest.getInstance("SHA-256")
                messageDigest.update(str.toByteArray(StandardCharsets.UTF_8))
                encodeStr = byte2Hex(messageDigest.digest())
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
            return encodeStr
        }

        private fun byte2Hex(bytes: ByteArray): String {
            val stringBuilder = StringBuilder()
            var temp: String
            for (aByte in bytes) {
                temp = Integer.toHexString(aByte.toInt() and 0xFF)
                if (temp.length == 1) {
                    stringBuilder.append("0")
                }
                stringBuilder.append(temp)
            }
            return stringBuilder.toString()
        }
    }

    private val initDatabase = InitDatabase(this)

    private fun check(account: String, password: String): Boolean {
        val userData = ArrayList<String>()
        val database = initDatabase.readableDatabase
        val cursor: Cursor = database.query(
            "user", null, null, null,
            null, null, null
        )
        while (cursor.moveToNext()) {
            if (cursor.getColumnIndex("userAccount") >= 0 && cursor.getColumnIndex("userPassword") >= 0) {
                @SuppressLint("Range") val userAccount =
                    cursor.getString(cursor.getColumnIndex("userAccount"))
                @SuppressLint("Range") val userPassword =
                    cursor.getString(cursor.getColumnIndex("userPassword"))
                userData.add(userAccount + userPassword)
            }
        }
        cursor.close()
        database.close()
        return userData.contains(account + getSha256(password))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.log_in)

        findViewById<View>(R.id.log_in).setOnClickListener {
            val account = findViewById<EditText>(R.id.account_log_in)
            val password = findViewById<EditText>(R.id.password_log_in)
            if (check(account.text.toString(), password.text.toString())) {
                println("------------------行-----------------------------------------------------------------")
            }
        }

        findViewById<View>(R.id.textView_turn_signIn).setOnClickListener {
            val intent = Intent(this@LogIn, SignIn::class.java)
            startActivity(intent)
        }
    }
}
