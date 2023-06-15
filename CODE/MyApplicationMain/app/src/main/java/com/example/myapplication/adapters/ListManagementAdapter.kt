package com.example.myapplication.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.EmptyPage
import com.example.myapplication.InitDatabase
import com.example.myapplication.LogIn
import com.example.myapplication.R
import com.example.myapplication.models.ListPlan
import java.io.Serializable

class ListManagementAdapter(
    private val listData: ArrayList<ListPlan>,
    private val initDatabase: InitDatabase,
    private val context: Context
) : RecyclerView.Adapter<ListManagementAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.plan_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listData[position], position)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val listName: TextView = itemView.findViewById(R.id.listName)
        private val deleteButton: Button = itemView.findViewById(R.id.DeleteButton)
        private val scrollView: HorizontalScrollView = itemView.findViewById(R.id.scrollView_hor)

        init {
            itemView.setOnClickListener(this)
            listName.setOnClickListener(this)
            deleteButton.setOnClickListener {
                val database = initDatabase.writableDatabase
                database.execSQL("DELETE FROM doctor_" + LogIn.accountDoctor + " WHERE plans = '" + listName.text.toString().trim() + "'")
                listData.removeAt(adapterPosition)
                notifyDataSetChanged()
            }
        }

        fun bind(listData: ListPlan, position: Int) {
            listName.text = listData.title

            val openedItem = -1
            if (position == openedItem) {
                scrollView.post {
                    scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
                }
            } else {
                scrollView.post {
                    scrollView.fullScroll(HorizontalScrollView.FOCUS_LEFT)
                }
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(v: View) {
            notifyDataSetChanged()
            val intent = Intent(context, EmptyPage::class.java)
            val bundle = Bundle()
            val plan = LogIn.accountDoctor?.let { ListPlan(listName.text.toString(), it) }
            bundle.putSerializable("plan", plan as Serializable)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }
}
