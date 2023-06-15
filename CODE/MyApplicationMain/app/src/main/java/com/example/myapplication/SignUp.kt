package com.example.myapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.regex.Pattern

class SignUp : AppCompatActivity() {

    private var checkAccount = false
    private var checkPassword = false

    private val initDatabase = InitDatabase(this)

    private fun checkAccount(account: String, accountList: ArrayList<String>): String {
        val pattern = Pattern.compile("^[a-zA-Z0-9_]+$")
        val matcher = pattern.matcher(account)
        return when {
            account.isEmpty() -> "null"
            !matcher.matches() -> "strange"
            account.length >= 16 || account.length < 8 -> "length"
            accountList.contains(account) -> "include"
            else -> "accepted"
        }
    }

    private fun checkPassword(password: String): Boolean {
        return password.length in 8..16
    }

    private fun checkPasswordAgain(password: String, passwordAgain: String): Boolean {
        return Objects.equals(password, passwordAgain)
    }

    private fun getSha256(str: String): String {
        val messageDigest: MessageDigest
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

    private fun showAlertDialog(title: String, content: String, success: Boolean) {
        val dialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(content)
            .setPositiveButton("确定") { dialogInterface: DialogInterface?, i: Int ->
                if (success) {
                    startActivity(Intent(this@SignUp, LogIn::class.java))
                }
            }
            .create()
        dialog.show()
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.font_color))
    }

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()

        val accountList = ArrayList<String>()
        val database: SQLiteDatabase = initDatabase.readableDatabase
        val registerButton: Button = findViewById(R.id.register)
        registerButton.isEnabled = false
        val cursor: Cursor = database.query("user", null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            val userAccount: String? = cursor.getString(cursor.getColumnIndex("userAccount"))
            userAccount?.let { accountList.add(it) }
        }
        cursor.close()
        database.close()

        val account: TextInputLayout = findViewById(R.id.resetTitleLayout)
        account.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            @SuppressLint("ResourceAsColor")
            override fun afterTextChanged(editable: Editable) {
                val textView: TextView = findViewById(R.id.account_alert_sign_up)
                val feedback = checkAccount(account.editText?.text.toString().trim(), accountList)
                when (feedback) {
                    "null" -> {
                        account.error = null
                        textView.text = " "
                    }
                    "strange" -> {
                        account.error = "账号不能包含特殊字符"
                        textView.setTextColor(Color.rgb(175, 0, 32))
                        textView.text = "账号不能包含特殊字符"
                        registerButton.isEnabled = false
                    }
                    "length" -> {
                        account.error = "账号长度应在8~16位之间"
                        textView.setTextColor(Color.rgb(175, 0, 32))
                        textView.text = "账号长度应在8~16位之间"
                        registerButton.isEnabled = false
                    }
                    "include" -> {
                        account.error = "该账号已经存在"
                        textView.setTextColor(Color.rgb(175, 0, 32))
                        textView.text = "该账号已经存在"
                        registerButton.isEnabled = false
                    }
                    "accepted" -> {
                        account.error = null
                        textView.setTextColor(R.color.accepted_color)
                        textView.text = "accepted"
                        checkAccount = true
                    }
                }
            }
        })

        val password: TextInputLayout = findViewById(R.id.password_layout_sign_up)
        password.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            @SuppressLint("ResourceAsColor")
            override fun afterTextChanged(editable: Editable) {
                val textView: TextView = findViewById(R.id.password_sign_up_alert)
                if (password.editText?.text.toString().trim().isEmpty()) {
                    textView.text = " "
                    password.error = null
                } else if (!checkPassword(password.editText?.text.toString().trim())) {
                    textView.setTextColor(Color.rgb(175, 0, 32))
                    textView.text = "密码长度应在8~16位之间"
                    password.error = "密码长度应在8~16位之间"
                    registerButton.isEnabled = false
                } else {
                    password.error = null
                    textView.setTextColor(R.color.accepted_color)
                    textView.text = "accepted"
                    checkPassword = true
                }
            }
        })

        val passwordAgain: TextInputLayout = findViewById(R.id.password_again_layout_sign_up)
        passwordAgain.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            @SuppressLint("ResourceAsColor")
            override fun afterTextChanged(editable: Editable) {
                val textView: TextView = findViewById(R.id.password_sign_up_again_alert)
                if (passwordAgain.editText?.text.toString().trim().isEmpty()) {
                    textView.text = " "
                    passwordAgain.error = null
                } else if (!checkPasswordAgain(
                        password.editText?.text.toString().trim(),
                        passwordAgain.editText?.text.toString().trim()
                    )
                ) {
                    passwordAgain.error = "两次密码输入需一致"
                    textView.setTextColor(Color.rgb(175, 0, 32))
                    textView.text = "两次密码输入需一致"
                    registerButton.isEnabled = false
                } else {
                    passwordAgain.error = null
                    textView.setTextColor(R.color.accepted_color)
                    textView.text = "accepted"
                    if (checkAccount && checkPassword) {
                        registerButton.isEnabled = true
                        registerButton.setBackgroundColor(R.color.purple_200)
                    }
                }
            }
        })

        val code: EditText = findViewById(R.id.verificationCode_sign_in)
        val codeAlert: TextView = findViewById(R.id.verificationCode_sign_in_alert)
        val isDoctor: CheckBox = findViewById(R.id.isDoctor_sign_in)
        code.visibility = View.GONE
        codeAlert.visibility = View.GONE

        findViewById<View>(R.id.isDoctor_sign_in).setOnClickListener { view ->
            if (isDoctor.isChecked) {
                code.visibility = View.VISIBLE
                codeAlert.visibility = View.VISIBLE
            } else {
                code.visibility = View.GONE
                codeAlert.visibility = View.GONE
            }
        }

        findViewById<View>(R.id.register).setOnClickListener { v ->
            if (isDoctor.isChecked) {
                if (code.text.toString() == "seele") {
                    val database: SQLiteDatabase = initDatabase.getWritableDatabase() // 这是一个对数据库操作的对象
                    database.execSQL(
                        "insert into doctor (doctorAccount, doctorPassword) values ('" + account.editText?.text.toString()
                            .trim { it <= ' ' } + "','" + getSha256(password.editText?.text.toString()) + "')"
                    )
                    database.execSQL(
                        "create table if not exists doctor_" + account.editText?.text.toString()
                            .trim { it <= ' ' } + " (plans varchar(50), plan_data varchar(10000))"
                    )
                    showAlertDialog("成功", "注册医生账号成功", true)
                } else showAlertDialog("失败", "邀请码错误", false)
            } else {
                val database: SQLiteDatabase = initDatabase.getWritableDatabase() // 这是一个对数据库操作的对象
                database.execSQL(
                    "insert into user (userAccount, userPassword) values ('" + account.editText?.text.toString()
                        .trim { it <= ' ' } + "','" + getSha256(password.editText?.text.toString()) + "')"
                )
                showAlertDialog("成功", "注册用户账号成功", true)
            }
        }
    }
}
