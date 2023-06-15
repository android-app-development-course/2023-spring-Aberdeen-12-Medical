package com.example.myapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.models.ListCommunication

class ListCommunicationAdapter(context: Context, dataArrayList: ArrayList<ListCommunication>) :
    ArrayAdapter<ListCommunication>(context, R.layout.communication_list, dataArrayList) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var convertView = view
        val listData = getItem(position)

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.communication_list, parent, false)
        }

        val listTitle: TextView = convertView!!.findViewById(R.id.communication_title)
        val listTime: TextView = convertView.findViewById(R.id.communication_time)
        val listAuthor: TextView = convertView.findViewById(R.id.communication_author)
        val listIdentity: TextView = convertView.findViewById(R.id.communication_identity)

        listTitle.text = listData?.title
        listTime.text = listData?.time
        listAuthor.text = listData?.author
        listIdentity.text = listData?.identity

        return convertView
    }
}
