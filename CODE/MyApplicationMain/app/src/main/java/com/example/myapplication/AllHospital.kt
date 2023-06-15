package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.ListAllAdapter
import com.example.myapplication.models.ListAll
import com.google.android.material.appbar.AppBarLayout
import java.util.ArrayList

class AllHospital : AppCompatActivity() {
    private lateinit var initDatabase: InitDatabase
    private val information: ArrayList<ListAll> = ArrayList()
    private lateinit var adapter: ListAllAdapter

    @SuppressLint("NotifyDataSetChanged", "Range")
    private fun initTheDatabase() {
        information.clear()
        val database: SQLiteDatabase = initDatabase.readableDatabase
        val cursor: Cursor = database.query(
            "doctor", null, null, null, null, null, null
        )
        while (cursor.moveToNext()) {
            val account: String = cursor.getString(cursor.getColumnIndex("doctorAccount"))
            val title: String = cursor.getString(cursor.getColumnIndex("Title"))
            val address: String = cursor.getString(cursor.getColumnIndex("Address"))
            val listData = ListAll(account, title, address)
            information.add(listData)
        }
        adapter.notifyDataSetChanged()
        cursor.close()
        database.close()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_hospital)

        findViewById<View>(R.id.back).setOnClickListener { finish() }

        initDatabase = InitDatabase(this)
        initTheDatabase()
        val recyclerView: RecyclerView = findViewById(R.id.all_recycleView)
        adapter = ListAllAdapter(information, initDatabase, this)
        recyclerView.adapter = adapter
        recyclerView.isClickable = true

        val searchView: SearchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            @SuppressLint("NotifyDataSetChanged", "Range")
            override fun onQueryTextSubmit(s: String): Boolean {
                information.clear()
                val initDatabase = InitDatabase(this@AllHospital)
                val database = initDatabase.readableDatabase
                val cursor = database.query(
                    "doctor", null, "Title LIKE ?", arrayOf("%$s%"), null, null, null
                )
                while (cursor.moveToNext()) {
                    val account: String = cursor.getString(cursor.getColumnIndex("doctorAccount"))
                    val title: String = cursor.getString(cursor.getColumnIndex("Title"))
                    val address: String = cursor.getString(cursor.getColumnIndex("Address"))
                    val listData = ListAll(account, title, address)
                    information.add(listData)
                }
                cursor.close()
                database.close()
                adapter.notifyDataSetChanged()
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                if (TextUtils.isEmpty(s)) {
                    initTheDatabase()
                }
                return false
            }
        })
    }
}
