package com.boostcamp.travery.presentation.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.boostcamp.travery.GlideApp
import com.boostcamp.travery.R
import kotlinx.android.synthetic.main.item_viewpager_image.view.*

class ViewPagerAdapter(private val images: ArrayList<String>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.item_viewpager_image, container, false)
        if (images[position].contains("android", false)) {
            GlideApp.with(container.context)
                    .load(images[position])
                    .centerCrop()
                    .into(view.iv_image)
        } else {
            GlideApp.with(container.context)
                    .load(container.context.getString(R.string.base_image_url) + images[position])
                    .centerCrop()
                    .into(view.iv_image)
        }

        container.addView(view)
        return view
    }

    override fun getCount(): Int {
        return images.size
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.invalidate()
    }

    fun itemChange(images: List<String>) {
        this.images.clear()
        this.images.addAll(images)
        notifyDataSetChanged()
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    fun clear() {
        this.images.clear()
        notifyDataSetChanged()
    }
}