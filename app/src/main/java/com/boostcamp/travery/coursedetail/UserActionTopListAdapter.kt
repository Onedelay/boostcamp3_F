package com.boostcamp.travery.coursedetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.travery.R
import com.boostcamp.travery.base.ObservableRecyclerViewAdapter
import com.boostcamp.travery.data.model.UserAction
import com.boostcamp.travery.databinding.ItemUseractionDetailToplistBinding
import com.boostcamp.travery.databinding.ItemUseractionEmptyBinding
import com.boostcamp.travery.utils.toImage

class UserActionTopListAdapter(userActionList: ObservableList<UserAction?>) :
        ObservableRecyclerViewAdapter<UserAction?, RecyclerView.ViewHolder>(userActionList) {
    companion object {
        const val TYPE_ACTION = 0
        const val TYPE_EMPTY_ACTIVITY = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_ACTION) {
            return ActivityViewHolder(ItemUseractionDetailToplistBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            return ActivityEmptyViewHolder(ItemUseractionEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ActivityEmptyViewHolder) {
            //아이템이 0번째라면 시작 이미지
            if (position == 0) {
                holder.binding.ivEndpoint.setImageResource(R.drawable.start)
            } else {
                holder.binding.ivEndpoint.setImageResource(R.drawable.finish)
            }
            holder.binding.userAction = getItem(position)
        } else if (holder is ActivityViewHolder) {
            val tens = position % 100 / 10
            val units = position % 10
            holder.binding.ivTens.setImageResource(tens.toImage())
            holder.binding.ivUnits.setImageResource(units.toImage())
            holder.binding.userAction = getItem(position)
            holder.binding.executePendingBindings()
        }

    }


    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) != null) TYPE_ACTION else TYPE_EMPTY_ACTIVITY
    }

    //기록된 활동들에 대한 뷰홀더
    class ActivityViewHolder(var binding: ItemUseractionDetailToplistBinding) : RecyclerView.ViewHolder(binding.root)

    //시작점과 끝점 아이템을 위한 뷰홀더
    class ActivityEmptyViewHolder(var binding: ItemUseractionEmptyBinding) : RecyclerView.ViewHolder(binding.root)


}