package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.example.myapplication.adapters.ListCommunicationAdapter
import com.example.myapplication.databinding.ActivityMainComunicationBinding
import com.example.myapplication.models.ListCommunication
import java.util.ArrayList

class MainCommunication : Fragment() {

    private lateinit var binding: ActivityMainComunicationBinding
    private lateinit var adapter: ListCommunicationAdapter
    private val arrayList = ArrayList<ListCommunication>()
    private lateinit var listData: ListCommunication

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityMainComunicationBinding.inflate(inflater, container, false)
        val view = binding.root

        val titleList = arrayOf(
            "给大家分享一个广州三甲医院的流程图",
            "急急急，哪里可以买到医保",
            "去哪里有比较好的心理医院呀"
        )
        val authorList = arrayOf("lhy", "Alex", "Trython")
        val timeList = arrayOf("2023/4/07", "2023/4/26", "2023/4/13")
        val identityList = arrayOf("分享·非官方", "问答·非官方", "问答·非官方")
        for (i in titleList.indices) {
            listData = ListCommunication(titleList[i], authorList[i], timeList[i], identityList[i])
            listData.ID = 1
            arrayList.add(listData)
        }
        val activity: Activity? = activity
        activity?.let {
            adapter = ListCommunicationAdapter(it, arrayList)
            binding.listView.adapter = adapter
            binding.listView.isClickable = true
            binding.listView.onItemClickListener =
                AdapterView.OnItemClickListener { adapterView, view, i, l ->
                    val intent = Intent(activity, EmptyPage::class.java)
                    intent.putExtra("ID", arrayList[i].ID.toString())
                    startActivity(intent)
                }
        }
        return view
    }
}
