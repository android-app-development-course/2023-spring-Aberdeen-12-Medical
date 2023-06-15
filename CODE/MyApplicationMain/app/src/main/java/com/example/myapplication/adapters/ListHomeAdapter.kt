package com.example.myapplication.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ContentHospital
import com.example.myapplication.InitDatabase
import com.example.myapplication.R
import com.example.myapplication.models.ListHome

class ListHomeAdapter(
    private val listData: ArrayList<ListHome>,
    private val initDatabase: InitDatabase,
    private val context: Context
) : RecyclerView.Adapter<ListHomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_hospital_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listData[position], position)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private lateinit var account: String
        private val name: TextView = itemView.findViewById(R.id.homeHospitalName)
        private val image: ImageView = itemView.findViewById(R.id.homeHospitalImage)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(listData: ListHome, position: Int) {
            name.text = listData.hospitalName
            image.setImageDrawable(listData.hospitalImage)
            account = listData.account
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(v: View) {
            val intent = Intent(context, ContentHospital::class.java)
            intent.putExtra("Account", account)
            context.startActivity(intent)
        }
    }
}
