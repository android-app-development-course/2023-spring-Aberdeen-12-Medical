package com.example.myapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class SignIn : AppCompatActivity() {
    private var checkAccount = false
    private var checkPassword = false
    private var checkPasswordAgain = false

    private val initDatabase = InitDatabase(this)

    private fun checkAccount(account: String): Boolean {
        val pattern: Pattern = Pattern.compile("^[a-zA-Z0-9_]+$")
        //testStr被检测的文本
        val matcher: Matcher = pattern.matcher(account)

        val userData = ArrayList<String>()
        val database: SQLiteDatabase = initDatabase.readableDatabase // 这是一个对数据库操作的对象
        val cursor: Cursor = database.query("user", null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            if (cursor.getColumnIndex("userAccount") >= 0 && cursor.getColumnIndex("userPassword") >= 0) {
                @SuppressLint("Range") val userAccount =
                    cursor.getString(cursor.getColumnIndex("userAccount"))
                userData.add(userAccount)
            }
        }

        cursor.close()
        database.close()

        return matcher.matches() && account.length <= 16 && account.length >= 8 && !userData.contains(
            account
        )
    }

    private fun checkPassword(password: String): Boolean {
        return password.length >= 8 && password.length <= 16
    }

    private fun checkPasswordAgain(password: String, passwordAgain: String): Boolean {
        return Objects.equals(
            password,
            passwordAgain
        ) && passwordAgain.length >= 8 && passwordAgain.length <= 16
    }

    // 我这里写了一个加密 加密用户的密码
    fun getSha256(str: String): String {
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

    // 提示弹窗开始
    private fun showAlertDialog(title: String, content: String, success: Boolean) {
        val dialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(content)
            .setPositiveButton("确定") { dialogInterface: DialogInterface, i: Int ->
                if (success) {
                    startActivity(Intent(this@SignIn, LogIn::class.java))
                }
            }
            .create()
        // 展示后再更改 就不会导致空指针异常了
        dialog.show()
        dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            .setTextColor(resources.getColor(R.color.font_color))
    }

// 提示弹窗结束

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in)
        findViewById<View>(R.id.account_sign_in).setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                // 此处为失去焦点时的处理内容
                val account = findViewById<EditText>(R.id.account_sign_in)
                if (checkAccount(account.text.toString())) {
                    val textView = findViewById<TextView>(R.id.account_sign_in_Alert)
                    textView.setTextColor(Color.rgb(26, 84, 152))
                    textView.text = "accepted"
                    checkAccount = true
                } else {
                    val textView = findViewById<TextView>(R.id.account_sign_in_Alert)
                    textView.setTextColor(android.graphics.Color.RED)
                    textView.text = "账号格式有误"
                    checkAccount = false
                }
            }
        }

        findViewById<View>(R.id.password_sign_in).setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                // 此处为失去焦点时的处理内容
                val password = findViewById<EditText>(R.id.password_sign_in)
                if (checkPassword(password.text.toString())) {
                    val textView = findViewById<TextView>(R.id.password_sign_in_Alert_1)
                    textView.setTextColor(Color.rgb(26, 84, 152))
                    textView.text = "accepted"
                    checkPassword = true
                } else {
                    val textView = findViewById<TextView>(R.id.password_sign_in_Alert_1)
                    textView.setTextColor(android.graphics.Color.RED)
                    textView.text = "密码格式有误"
                    checkPassword = false
                }
            }
        }

// v 表示view
        findViewById<View>(R.id.password_sign_in_Again).setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                // 此处为失去焦点时的处理内容
                val password = findViewById<EditText>(R.id.password_sign_in)
                val passwordAgain = findViewById<EditText>(R.id.password_sign_in_Again)
                if (checkPasswordAgain(password.text.toString(), passwordAgain.text.toString())) {
                    val textView = findViewById<TextView>(R.id.password_sign_in_Alert_2)
                    textView.setTextColor(Color.rgb(26, 84, 152))
                    textView.text = "accepted"
                    checkPasswordAgain = true
                } else {
                    val textView = findViewById<TextView>(R.id.password_sign_in_Alert_2)
                    textView.setTextColor(android.graphics.Color.RED)
                    textView.text = "两次密码必须相同"
                    checkPasswordAgain = false
                }
            }
        }


        findViewById<View>(R.id.register).setOnClickListener {
            // 先失去焦点
            findViewById<View>(R.id.account_sign_in).clearFocus()
            findViewById<View>(R.id.password_sign_in).clearFocus()
            findViewById<View>(R.id.password_sign_in_Again).clearFocus()

            val account = findViewById<EditText>(R.id.account_sign_in)
            val password = findViewById<EditText>(R.id.password_sign_in)

            if (checkAccount && checkPassword && checkPasswordAgain) {
                println("-----------123---------------------ok---------------")
                val database = initDatabase.writableDatabase // 这是一个对数据库操作的对象
                database.execSQL(
                    "insert into user (userAccount, userPassword) values ('${account.text}','${
                        getSha256(
                            password.text.toString()
                        )
                    }')"
                )
                showAlertDialog("成功", "注册账号成功", true)
            } else {
                showAlertDialog("失败", "注册账号失败请检查输入的账号和密码", false)
            }
        }
    }
}