package com.example.myapplication

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteStatement
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ImageSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets
import java.sql.PreparedStatement
import java.util.ArrayList
import java.util.List
import java.util.Objects
import java.util.regex.Matcher
import java.util.regex.Pattern

class AddPlan : AppCompatActivity() {
    private lateinit var container: EditText
    private lateinit var initDatabase: InitDatabase
    private lateinit var database: SQLiteDatabase

    @SuppressLint("MissingInflatedId", "Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_plan)

        initDatabase = InitDatabase(this)
        database = initDatabase.writableDatabase
        container = findViewById(R.id.contentEdit)
        val editText = findViewById<EditText>(R.id.newPath)

        findViewById<View>(R.id.addImage).setOnClickListener { view: View? ->
            // 打开系统文件选择器
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        findViewById<View>(R.id.upload).setOnClickListener { view: View? ->
            val plans = ArrayList<String>()
            val cursorPlans = database.query(
                "doctor_" + LogIn.accountDoctor, null, null, null, null, null, null
            )
            while (cursorPlans.moveToNext()) {
                if (cursorPlans.getColumnIndex("plans") >= 0) {
                    val plan = cursorPlans.getString(cursorPlans.getColumnIndex("plans"))
                    plans.add(plan)
                }
            }
            cursorPlans.close()
            val title = editText.text.toString().trim { it <= ' ' }
            if (title.length != 0 && !plans.contains(editText.text.toString())) {
                val builder = SpannableStringBuilder(container.editableText)
                val spans = builder.getSpans(0, builder.length, ImageSpan::class.java)
                if (spans != null) {
                    for (span in spans) {
                        val start = builder.getSpanStart(span)
                        val end = builder.getSpanEnd(span)
                        val drawable = span.drawable.toString()
                        builder.replace(start, end, "[image]" + drawable.substring(drawable.indexOf("@") + 1) + "[/image]")
                    }
                }
                val source = builder.toString() // 将图片替换成文字后的文本
                val statement: SQLiteStatement = database.compileStatement("INSERT INTO doctor_"+LogIn.accountDoctor+" (plans, plan_data) VALUES (?,?)")
                statement.bindString(1, title)
                statement.bindString(2, source)
                statement.executeInsert()
                container.editableText.clear()
            } else {
                val text = "存在该路线"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(this@AddPlan, text, duration)
                toast.show()
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                // 获取选择的图片的 Uri
                val selectedImageUri: Uri? = Objects.requireNonNull(data)?.data
                try {
                    // 将 Uri 转换成 Drawable
                    val inputStream: InputStream? = contentResolver.openInputStream(selectedImageUri!!)
                    // 将InputStream转换为字节数组
                    val bytes: ByteArray? = inputStream?.readBytes()
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes!!.size)
                    val drawable = BitmapDrawable(resources, bitmap)

                    val statement: SQLiteStatement = database.compileStatement("INSERT INTO images (photoName, source) VALUES (?,?)")
                    statement.bindString(1, drawable.toString().substring(drawable.toString().indexOf("@")+1))
                    statement.bindBlob(2, bytes)
                    statement.executeInsert()

                    // 在 EditText 中显示图片
                    drawable.setBounds(0, 0, drawable.intrinsicWidth / 2, drawable.intrinsicHeight / 2)
                    val imageString = SpannableString("带图片的文本")
                    imageString.setSpan(ImageSpan(drawable), 0, imageString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                    // 获取 EditText 中已有的内容
                    val editable: Editable = container.editableText

                    // 记录插入图片前光标位置
                    val start: Int = container.selectionStart

                    // 将插入的图片插入到光标位置
                    editable.insert(start, imageString)

                    // 将光标移动到插入图片后面
                    container.setSelection(start + 1)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}
