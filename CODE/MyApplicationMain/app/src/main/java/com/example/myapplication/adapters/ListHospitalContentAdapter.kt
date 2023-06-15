package com.example.myapplication.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.EmptyPage
import com.example.myapplication.InitDatabase
import com.example.myapplication.R
import com.example.myapplication.models.ListPlan
import java.io.Serializable

class ListHospitalContentAdapter(
    private val listData: ArrayList<ListPlan>,
    private val account: String,
    private val initDatabase: InitDatabase,
    private val context: Context
) : RecyclerView.Adapter<ListHospitalContentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.content_hospital_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listData[position], position)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val title: TextView = itemView.findViewById(R.id.pathTitle)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(listData: ListPlan, position: Int) {
            title.text = listData.title
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(v: View) {
            val intent = Intent(context, EmptyPage::class.java)
            val bundle = Bundle()
            val plan = ListPlan(title.text.toString(), account)
            bundle.putSerializable("plan", plan as Serializable)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }
}
