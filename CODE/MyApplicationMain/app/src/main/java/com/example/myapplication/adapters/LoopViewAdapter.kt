package com.example.myapplication.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import com.example.myapplication.R

class LoopViewAdapter(private val imageViewList: ArrayList<ImageView>) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val newPosition = position % imageViewList.size
        val image = imageViewList[newPosition]
        container.addView(image)
        return image
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return Int.MAX_VALUE
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    inner class PagerOnClickListener(private val mContext: Context) : View.OnClickListener {
        override fun onClick(v: View) {
            when (v.id) {
                R.id.pager_img1 -> Toast.makeText(mContext, "图片1被点击", Toast.LENGTH_SHORT).show()
                R.id.pager_img2 -> Toast.makeText(mContext, "图片2被点击", Toast.LENGTH_SHORT).show()
                R.id.pager_img3 -> Toast.makeText(mContext, "图片3被点击", Toast.LENGTH_SHORT).show()
                R.id.pager_img4 -> Toast.makeText(mContext, "图片4被点击", Toast.LENGTH_SHORT).show()
                R.id.pager_img5 -> Toast.makeText(mContext, "图片5被点击", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
