package com.example.myapplication.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ContentHospital
import com.example.myapplication.InitDatabase
import com.example.myapplication.R
import com.example.myapplication.models.ListAll

class ListAllAdapter(
    private val listData: ArrayList<ListAll>,
    private val initDatabase: InitDatabase,
    private val context: Context
) : RecyclerView.Adapter<ListAllAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_hospital_list, parent, false)
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
        private val title: TextView = itemView.findViewById(R.id.hospitalTitle)
        private val address: TextView = itemView.findViewById(R.id.hospitalAddress)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(listData: ListAll, position: Int) {
            title.text = listData.hospitalTitle
            address.text = listData.hospitalAddress
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
