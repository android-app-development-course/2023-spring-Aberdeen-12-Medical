package com.example.myapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.L
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Reset : AppCompatActivity() {

    private val initDatabase = InitDatabase(this)

    private fun showAlertDialog(title: String, content: String) {
        val dialog: AlertDialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(content)
            .setPositiveButton("确定") { dialogInterface: DialogInterface?, i: Int ->
                startActivity(Intent(this@Reset, MainHospital::class.java))
            }
            .create()
        // 展示后再更改 就不会导致空指针异常了
        dialog.show()
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.font_color))
    }

    @SuppressLint("WrongViewCast", "MissingInflatedId", "Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)

        val title: TextInputLayout = findViewById(R.id.resetTitleLayout)
        val address: TextInputLayout = findViewById(R.id.resetAddressLayout)
        val phone: TextInputLayout = findViewById(R.id.resetPhoneLayout)
        val official: TextInputLayout = findViewById(R.id.resetOfficialLayout)

        val database: SQLiteDatabase = initDatabase.writableDatabase
        val projection = arrayOf("Title", "Address", "Phone", "Official,Photo")
        val selection = "doctorAccount = ?"
        val selectionArgs = arrayOf(LogIn.accountDoctor)

        val cursor: Cursor = database.query("doctor", projection, selection, selectionArgs, null, null, null)
        while (cursor.moveToNext()) {
            val cursorTitle: String? = cursor.getString(cursor.getColumnIndex("Title"))
            val cursorAddress: String? = cursor.getString(cursor.getColumnIndex("Address"))
            val cursorPhone: String? = cursor.getString(cursor.getColumnIndex("Phone"))
            val cursorOfficial: String? = cursor.getString(cursor.getColumnIndex("Official"))
            if (cursorTitle != null) {
                title.editText?.setText(cursorTitle)
            }
            if (cursorAddress != null) {
                address.editText?.setText(cursorAddress)
            }
            if (cursorPhone != null) {
                phone.editText?.setText(cursorPhone)
            }
            if (cursorOfficial != null) {
                official.editText?.setText(cursorOfficial)
            }
        }

        findViewById<Button>(R.id.updateButton).setOnClickListener {
            database.execSQL("UPDATE doctor SET " +
                    "Title = '" + title.editText?.text.toString().trim() + "', " +
                    "Address = '" + address.editText?.text.toString().trim() + "', " +
                    "Phone = '" + phone.editText?.text.toString().trim() + "', " +
                    "Official = '" + official.editText?.text.toString().trim() + "' " +
                    "where doctorAccount = " + LogIn.accountDoctor + "")
            showAlertDialog("成功", "数据更新成功")
        }
    }
}
