package com.example.myapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.collections.ArrayList

class LogIn : AppCompatActivity() {

    companion object {
        var accountDoctor: String? = null
        var accountUser: String? = null
    }

    private val initDatabase = InitDatabase(this)
    private var loseNumber = 0

    private fun showAlertDialog(title: String, content: String, success: Boolean) {
        val dialog: AlertDialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(content)
            .setPositiveButton("确定",
                DialogInterface.OnClickListener { dialogInterface: DialogInterface?, i: Int ->
                    if (success) {
                        val account = findViewById<EditText>(R.id.account_log_in)
                        val password = findViewById<EditText>(R.id.password_log_in)
                        account.setText("")
                        password.setText("")
                    }
                })
            .create()
        //    展示后再更改 就不会导致空指针异常了
        dialog.show()
        dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            .setTextColor(resources.getColor(R.color.font_color))
    }

    private fun getSha256(str: String): String {
        var messageDigest: MessageDigest? = null
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
                //1得到一位的进行补0操作
                stringBuilder.append("0")
            }
            stringBuilder.append(temp)
        }
        return stringBuilder.toString()
    }

    private fun check(identity: String, account: String, password: String): Boolean {
        val userData = ArrayList<String>()
        val database: SQLiteDatabase = initDatabase.readableDatabase // 这是一个对数据库操作的对象
        val cursor: Cursor = database.query(identity, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            if (Objects.equals(identity, "user")) {
                if (cursor.getColumnIndex("userAccount") >= 0 && cursor.getColumnIndex("userPassword") >= 0) {
                    @SuppressLint("Range") val userAccount: String =
                        cursor.getString(cursor.getColumnIndex("userAccount"))
                    @SuppressLint("Range") val userPassword: String =
                        cursor.getString(cursor.getColumnIndex("userPassword"))
                    userData.add(userAccount + userPassword)
                }
            } else if (Objects.equals(identity, "doctor")) {
                if (cursor.getColumnIndex("doctorAccount") >= 0 && cursor.getColumnIndex("doctorPassword") >= 0) {
                    @SuppressLint("Range") val userAccount: String =
                        cursor.getString(cursor.getColumnIndex("doctorAccount"))
                    @SuppressLint("Range") val userPassword: String =
                        cursor.getString(cursor.getColumnIndex("doctorPassword"))
                    userData.add(userAccount + userPassword)
                }
            }
        }
        cursor.close()
        database.close()
        return userData.contains(account + getSha256(password))
    }

    private var time: Long = 0
    private lateinit var handler: Handler
    private val timerRunnable: Runnable = object : Runnable {
        override fun run() {
            time += 1000
            handler.postDelayed(this, 1000)
        }
    }

    private fun oneMinute() {
        handler = Handler()
        handler.postDelayed(timerRunnable, 1000)
        if (time >= 60000) {
            loseNumber = 0
            handler.removeCallbacks(timerRunnable)
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.hide()
        }
        val isDoctor = findViewById<CheckBox>(R.id.isDoctor_log_in)
        findViewById<View>(R.id.log_in).setOnClickListener(View.OnClickListener {
            val account = findViewById<TextInputEditText>(R.id.account_log_in)
            val password = findViewById<TextInputEditText>(R.id.password_log_in)
            if (account.length() != 0 && password.length() != 0) {
                if (isDoctor.isChecked) {
                    if (check(
                            "doctor",
                            account.text.toString().trim(),
                            password.text.toString().trim()
                        )
                    ) {
                        accountDoctor = account.text.toString().trim()
                        startActivity(Intent(this@LogIn, ManagementHospital::class.java))
                    } else {
                        if (loseNumber < 5) {
                            loseNumber += 1
                            showAlertDialog("错误", "账号密码错误", true)
                        } else {
                            showAlertDialog("错误", "密码错误次数超过五次\n请一分钟后再试", true)
                            oneMinute()
                        }
                    }
                } else {
                    if (check(
                            "user",
                            account.text.toString().trim(),
                            password.text.toString().trim()
                        )
                    ) {
                        accountUser = account.text.toString().trim()
                        startActivity(Intent(this@LogIn, Home::class.java))
                    } else {
                        if (loseNumber < 5) {
                            loseNumber += 1
                            showAlertDialog("错误", "账号密码错误", true)
                        } else {
                            showAlertDialog("错误", "密码错误次数超过五次\n请一分钟后再试", true)
                            oneMinute()
                        }
                    }
                }
            } else {
                showAlertDialog("提示", "请输入账号密码", false)
            }
        })
        findViewById<View>(R.id.textView_turn_signIn).setOnClickListener(View.OnClickListener {
            val intent = Intent(this@LogIn, SignUp::class.java)
            startActivity(intent)
        })
    }
}
