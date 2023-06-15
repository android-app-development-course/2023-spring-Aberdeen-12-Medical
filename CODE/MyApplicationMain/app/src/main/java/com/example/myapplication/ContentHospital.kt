package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.ListHospitalContentAdapter
import com.example.myapplication.models.ListAll
import com.example.myapplication.models.ListPlan
import com.hitomi.cmlibrary.CircleMenu
import com.hitomi.cmlibrary.OnMenuSelectedListener
import java.util.*

class ContentHospital : AppCompatActivity() {
    private var initDatabase: InitDatabase? = null
    private var plans: ArrayList<ListPlan>? = null
    private var adapter: ListHospitalContentAdapter? = null

    @SuppressLint("MissingInflatedId", "Range", "LocalSuppress")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_hospital)
        val intent = intent
        var listData: ListPlan
        val account = intent.getStringExtra("Account")
        val imageView = findViewById<ImageView>(R.id.hospital_content_imageView)

        initDatabase = InitDatabase(this)
        plans = ArrayList()
        adapter = ListHospitalContentAdapter(plans!!, account!!, initDatabase!!, this)

        val projection = arrayOf("Photo")
        val selection = "doctorAccount = ?"
        val selectionArgs = arrayOf(account)
        val database = initDatabase!!.readableDatabase
        val cursor = database.query("doctor", projection, selection, selectionArgs, null, null, null)
        while (cursor.moveToNext()) {
            val cursorImage = cursor.getBlob(cursor.getColumnIndex("Photo"))
            if (cursorImage != null) {
                val bitmap = BitmapFactory.decodeByteArray(cursorImage, 0, cursorImage.size)
                val drawable = BitmapDrawable(resources, bitmap)
                imageView.setImageDrawable(drawable)
                break
            }
        }
        cursor.close()
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        val recyclerView = findViewById<RecyclerView>(R.id.hospital_content_recycleView)
        val slideUp = AnimationUtils.loadAnimation(this@ContentHospital, R.anim.slide_up)
        slideUp.interpolator = OvershootInterpolator(1f)
        recyclerView.animation = slideUp
        val cursor1 = database.query("doctor_$account", null, null, null, null, null, null)
        while (cursor1.moveToNext()) {
            if (cursor1.getColumnIndex("plans") >= 0) {
                val plan = cursor1.getString(cursor1.getColumnIndex("plans"))
                listData = account?.let { ListPlan(plan, it) }!!
                plans!!.add(listData)
            }
        }
        cursor1.close()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.isClickable = true
        val circleMenu = findViewById<CircleMenu>(R.id.circleMenu)
        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.mipmap.outline, R.mipmap.fork)
            .addSubMenu(getColor(R.color.green_main), R.mipmap.back)
            .addSubMenu(getColor(R.color.cool_blue), R.mipmap.star)
            .addSubMenu(getColor(R.color.font_color), R.mipmap.star)
            .setOnMenuSelectedListener(object : OnMenuSelectedListener {
                override fun onMenuSelected(index: Int) {
                    when (index) {
                        0 -> finish()
                        1 -> Toast.makeText(this@ContentHospital, "收藏成功", Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
}
