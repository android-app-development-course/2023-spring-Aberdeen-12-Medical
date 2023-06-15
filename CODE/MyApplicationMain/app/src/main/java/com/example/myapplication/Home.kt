package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.myapplication.adapters.ListHomeAdapter
import com.example.myapplication.adapters.LoopViewAdapter
import com.example.myapplication.models.ListHome
import java.util.*

class Home : Fragment() {
    private lateinit var viewPager: ViewPager  //轮播图模块
    private lateinit var imageList: ArrayList<ImageView>
    private lateinit var ll_dots_container: LinearLayout
    private lateinit var loop_dec: TextView
    private var previousSelectedPosition = 0
    private var isRunning = false
    private lateinit var recyclerView: RecyclerView

    private fun initLoopView(view: View) {
        viewPager = view.findViewById(R.id.loopViewPager)
        ll_dots_container = view.findViewById(R.id.ll_dots_loop)
        loop_dec = view.findViewById(R.id.loop_dec)

        // 图片资源id数组
        val mImg = intArrayOf(
            R.drawable.hospital4,
            R.drawable.hospital4,
            R.drawable.hospital4,
            R.drawable.hospital4,
            R.drawable.hospital4
        )

        // 文本描述
        val mDec = arrayOf(
            "Test1",
            "Test2",
            "Test3",
            "Test4",
            "Test5"
        )

        val mImg_id = intArrayOf(
            R.id.pager_img1,
            R.id.pager_img2,
            R.id.pager_img3,
            R.id.pager_img4,
            R.id.pager_img5
        )

        // 初始化要展示的5个ImageView
        imageList = ArrayList()
        var imageView: ImageView
        var dotView: View
        var layoutParams: LinearLayout.LayoutParams
        for (i in mImg.indices) {
            //初始化要显示的图片对象
            imageView = ImageView(requireContext())
            imageView.setBackgroundResource(mImg[i])
            imageView.id = mImg_id[i]
            imageList.add(imageView)
            //加引导点
            dotView = View(requireContext())
            dotView.setBackgroundResource(R.drawable.dot)
            layoutParams = LinearLayout.LayoutParams(10, 10)
            if (i != 0) {
                layoutParams.leftMargin = 10
            }
            //设置默认所有都不可用
            dotView.isEnabled = false
            ll_dots_container.addView(dotView, layoutParams)
        }

        ll_dots_container.getChildAt(0).isEnabled = true
        loop_dec.text = mDec[0]
        previousSelectedPosition = 0
        //设置适配器
        viewPager.adapter = LoopViewAdapter(imageList)
        // 把ViewPager设置为默认选中Integer.MAX_VALUE / t2，从十几亿次开始轮播图片，达到无限循环目的;
        val m = (Integer.MAX_VALUE / 2) % imageList.size
        val currentPosition = Integer.MAX_VALUE / 2 - m
        viewPager.currentItem = currentPosition

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {}
            override fun onPageSelected(i: Int) {
                val newPosition = i % imageList.size
                loop_dec.text = mDec[newPosition]
                ll_dots_container.getChildAt(previousSelectedPosition).isEnabled = false
                ll_dots_container.getChildAt(newPosition).isEnabled = true
                previousSelectedPosition = newPosition
            }

            override fun onPageScrollStateChanged(i: Int) {}
        })

        // 开启轮询
        Thread {
            isRunning = true
            while (isRunning) {
                try {
                    Thread.sleep(5000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                val handler = Handler(Looper.getMainLooper())
                handler.post {
                    viewPager.currentItem = viewPager.currentItem + 1
                }
            }
        }.start()
    }

    @SuppressLint("Range", "UseCompatLoadingForDrawables", "Recycle")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val initDatabase = InitDatabase(requireContext())
        val information = ArrayList<ListHome>()
        var listData: ListHome
        val adapter = ListHomeAdapter(information, initDatabase, requireContext())

        val view = inflater.inflate(R.layout.activity_home, container, false)
        view.findViewById<View>(R.id.turn_all).setOnClickListener { view: View ->
            startActivity(Intent(view.context, AllHospital::class.java))
        }

        initLoopView(view)

        val database = initDatabase.readableDatabase
        val cursor = database.query("doctor", null, null, null, null, null, null)
        for (i in 0 until 4) {
            if (cursor.moveToNext()) {
                val account = cursor.getString(cursor.getColumnIndex("doctorAccount"))
                val title = cursor.getString(cursor.getColumnIndex("Title"))
                val photo = cursor.getBlob(cursor.getColumnIndex("Photo"))
                if (photo != null) {
                    val bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.size)
                    val drawable: Drawable = BitmapDrawable(resources, bitmap)
                    listData = ListHome(account, title, drawable)
                } else {
                    listData =
                        ListHome(account, title, resources.getDrawable(R.drawable.hospital4))
                }
                information.add(listData)
            }
        }

        recyclerView = view.findViewById(R.id.homeRecycleView)
        recyclerView.adapter = adapter
        recyclerView.isClickable = true

        return view
    }
}
