package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ImageSpan
import android.util.Base64
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.models.ListPlan
import com.hitomi.cmlibrary.CircleMenu
import com.hitomi.cmlibrary.OnMenuSelectedListener
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.ArrayList
import java.util.Objects
import java.util.SplittableRandom
import java.util.regex.Matcher
import java.util.regex.Pattern

class EmptyPage : AppCompatActivity() {

    private val initDatabase = InitDatabase(this)

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_empty)

        val bundle = intent.extras
        if (bundle != null) {
            val plan = bundle.getSerializable("plan") as? ListPlan
            val textView = findViewById<TextView>(R.id.emptyPage)

            val database = initDatabase.readableDatabase

            val cursor_1 = database.query("doctor_" + plan?.account, null, null, null, null, null, null)
            while (cursor_1.moveToNext()) {
                val name = cursor_1.getString(cursor_1.getColumnIndex("plans"))
                if (name == plan?.title) {
                    val data = cursor_1.getString(cursor_1.getColumnIndex("plan_data"))
                    val pattern = Pattern.compile("\\[image](.*?)\\[/image]")
                    val matcher = pattern.matcher(data)
                    val images = ArrayList<String>()
                    while (matcher.find()) {
                        val image = matcher.group(1)
                        images.add(image)
                    }
                    val replacedData = data.replace("[image]", "").replace("[/image]", "")
                    val builder = SpannableStringBuilder(replacedData)
                    val location = ArrayList<IntArray>()
                    for (i in images) {
                        val start = replacedData.indexOf(i)
                        val end = start + i.length
                        val indexes = intArrayOf(start, end)
                        location.add(indexes)
                    }

                    if (images.isNotEmpty()) {
                        for (i in images) {
                            val cursor_2 = database.query("images", null, null, null, null, null, null)
                            while (cursor_2.moveToNext()) {
                                val photoName = cursor_2.getString(cursor_2.getColumnIndex("photoName"))
                                if (i == photoName) {
                                    val photoSource = cursor_2.getBlob(cursor_2.getColumnIndex("source"))
                                    // 将字节数组解码为Bitmap对象
                                    val bitmap = BitmapFactory.decodeByteArray(photoSource, 0, photoSource.size)
                                    // 创建BitmapDrawable对象
                                    val drawable = BitmapDrawable(resources, bitmap)

                                    drawable.setBounds(0, 0, drawable.intrinsicWidth / 2, drawable.intrinsicHeight / 2)
                                    val imageString = SpannableString("带图片的文本")
                                    imageString.setSpan(ImageSpan(drawable), 0, imageString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                                    val lastOne = location.size - 1
                                    val popList = location.removeAt(lastOne)
                                    builder.replace(popList[0], popList[1], imageString)
                                    break
                                }
                            }
                        }
                        textView.text = builder
                    } else {
                        textView.text = replacedData
                    }
                }
            }
        }

        val circleMenu = findViewById<CircleMenu>(R.id.circleMenu)
        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.mipmap.outline, R.mipmap.fork)
            .addSubMenu(getColor(R.color.green_main), R.mipmap.back)
            .addSubMenu(getColor(R.color.cool_blue), R.mipmap.star)
            .addSubMenu(getColor(R.color.font_color), R.mipmap.star)
            .setOnMenuSelectedListener(object : OnMenuSelectedListener {
                override fun onMenuSelected(index: Int) {
                    when (index) {
                        0 -> {
                            println("0")
                            finish()
                        }
                        1 -> {
                            println("1")
                            Toast.makeText(this@EmptyPage, "收藏成功", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            })
    }

    override fun onBackPressed() {
        finish()
    }
}
