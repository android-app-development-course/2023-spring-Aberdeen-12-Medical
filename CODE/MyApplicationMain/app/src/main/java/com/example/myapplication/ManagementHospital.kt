package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.adapters.ListManagementAdapter
import com.example.myapplication.databinding.ActivityManagementHospitalBinding
import com.example.myapplication.models.ListPlan
import java.util.*

class ManagementHospital : AppCompatActivity() {

    private lateinit var binding: ActivityManagementHospitalBinding
    private val plans = ArrayList<ListPlan>()
    private val initDatabase = InitDatabase(this)
    private val adapter = ListManagementAdapter(plans, initDatabase, this)
    private lateinit var listData: ListPlan

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagementHospitalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database: SQLiteDatabase = initDatabase.readableDatabase
        val cursor: Cursor = database.query("doctor_" + LogIn.accountDoctor, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            if (cursor.getColumnIndex("plans") >= 0) {
                val plan = cursor.getString(cursor.getColumnIndex("plans"))
                listData = LogIn.accountDoctor?.let { ListPlan(plan, it) }!!
                plans.add(listData)
            }
        }

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.isClickable = true

        // 下拉刷新

        val swipeRefreshLayout: SwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setColorSchemeResources(R.color.green_main, R.color.white)
        swipeRefreshLayout.setOnRefreshListener {
            // 处理下拉刷新事件
            val database: SQLiteDatabase = initDatabase.readableDatabase
            val cursor: Cursor = database.query("doctor_" + LogIn.accountDoctor, null, null, null, null, null, null)
            plans.clear()
            while (cursor.moveToNext()) {
                if (cursor.getColumnIndex("plans") >= 0) {
                    val plan = cursor.getString(cursor.getColumnIndex("plans"))
                    listData = LogIn.accountDoctor?.let { ListPlan(plan, it) }!!
                    plans.add(listData)
                }
            }

            // 更新 RecyclerView 的数据源
            adapter.notifyDataSetChanged()

            // 刷新完成后调用 setRefreshing(false) 方法通知 SwipeRefreshLayout 停止刷新状态
            swipeRefreshLayout.isRefreshing = false
        }

        // 底部tag栏事件

        findViewById<View>(R.id.tagLayout2).setOnClickListener {
            finish()
            startActivity(Intent(this@ManagementHospital, AddPlan::class.java))
            println("第二个")
        }
        findViewById<View>(R.id.tagLayout3).setOnClickListener {
            finish()
            startActivity(Intent(this@ManagementHospital, MainHospital::class.java))
            println("第三个")
        }
    }
}
