package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteStatement
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.io.InputStream
import java.util.*

class MainHospital : AppCompatActivity() {
    private lateinit var initDatabase: InitDatabase
    private lateinit var imageView: ImageView
    private lateinit var database: SQLiteDatabase

    private fun showBottomDialog() {
        val dialog = Dialog(this, R.style.DialogTheme)
        val view = View.inflate(this, R.layout.popup_layout, null)
        dialog.setContentView(view)

        val window = dialog.window
        window?.setGravity(Gravity.BOTTOM)
        window?.setWindowAnimations(R.style.main_menu_animStyle)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.show()

        dialog.findViewById<View>(R.id.tv_take_pic).setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
            dialog.dismiss()
        }

        dialog.findViewById<View>(R.id.tv_cancel).setOnClickListener { dialog.dismiss() }
    }

    @SuppressLint("MissingInflatedId", "Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_hospital)
        initDatabase = InitDatabase(this)
        imageView = findViewById(R.id.hospitalImage)
        database = initDatabase.writableDatabase
        imageView.setOnClickListener { showBottomDialog() }

        findViewById<View>(R.id.hospital_exit).setOnClickListener {
            startActivity(Intent(this@MainHospital, MainActivity::class.java))
        }

        val title = findViewById<TextView>(R.id.hospitalName)
        val account = findViewById<TextView>(R.id.accountName)
        val address = findViewById<TextView>(R.id.hospitalAddressName)
        val phone = findViewById<TextView>(R.id.hospitalPhoneName)
        val projection = arrayOf("Title", "Address", "Phone", "doctorAccount, Photo")
        val selection = "doctorAccount = ?"
        val selectionArgs = arrayOf(LogIn.accountDoctor)

        val cursor = database.query("doctor", projection, selection, selectionArgs, null, null, null)
        while (cursor.moveToNext()) {
            val cursorTitle = cursor.getString(cursor.getColumnIndex("Title"))
            val cursorAddress = cursor.getString(cursor.getColumnIndex("Address"))
            val cursorPhone = cursor.getString(cursor.getColumnIndex("Phone"))
            val cursorAccount = cursor.getString(cursor.getColumnIndex("doctorAccount"))
            val cursorImage = cursor.getBlob(cursor.getColumnIndex("Photo"))
            if (cursorTitle != null) title.text = cursorTitle
            if (cursorAddress != null) address.text = cursorAddress
            if (cursorPhone != null) phone.text = cursorPhone
            if (cursorAccount != null) account.text = cursorAccount
            if (cursorImage != null) {
                val bitmap = BitmapFactory.decodeByteArray(cursorImage, 0, cursorImage.size)
                val drawable = BitmapDrawable(resources, bitmap)
                imageView.setImageDrawable(drawable)
            }
        }

        findViewById<View>(R.id.reset).setOnClickListener {
            startActivity(Intent(this@MainHospital, Reset::class.java))
        }

        findViewById<View>(R.id.tagLayout1).setOnClickListener {
            finish()
            startActivity(Intent(this@MainHospital, ManagementHospital::class.java))
            println("第一个")
        }
        findViewById<View>(R.id.tagLayout2).setOnClickListener {
            finish()
            startActivity(Intent(this@MainHospital, AddPlan::class.java))
            println("第二个")
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                val selectedImageUri: Uri? = Objects.requireNonNull(data)?.data
                try {
                    val inputStream: InputStream? = contentResolver.openInputStream(selectedImageUri!!)

                    val bytes: ByteArray? = inputStream?.readBytes()

                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes!!.size)
                    val drawable = BitmapDrawable(resources, bitmap)

                    imageView.setImageDrawable(drawable)

                    val statement: SQLiteStatement =
                        database.compileStatement("UPDATE doctor SET Photo = ? WHERE doctorAccount = ? ")
                    statement.bindBlob(1, bytes)
                    statement.bindString(2, LogIn.accountDoctor)
                    statement.executeInsert()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}
