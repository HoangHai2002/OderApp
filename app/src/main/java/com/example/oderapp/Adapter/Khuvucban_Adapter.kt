package com.example.oderapp.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.oderapp.Interface.itf_Click_khuVucBan
import com.example.oderapp.Interface.itf_UD_QLKVBan
import com.example.oderapp.Model.KhuVucBan
import com.example.oderapp.R
import com.example.oderapp.ViewHolder.Khuvucban_ViewHolder

class Khuvucban_Adapter(var listData : MutableList<KhuVucBan>, val listener : itf_Click_khuVucBan): RecyclerView.Adapter<Khuvucban_ViewHolder>() {
    var selectedPosition = RecyclerView.NO_POSITION
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Khuvucban_ViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_khuvucban, parent, false)
        return Khuvucban_ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    @SuppressLint("ResourceAsColor", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: Khuvucban_ViewHolder, position: Int) {
        var itemData = listData.get(position)
        holder.name.setText(itemData.name)
        if (position == selectedPosition) {
            holder.name.setBackgroundResource(R.drawable.background_khuvucban)
            holder.name.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        } else {
            holder.name.setBackgroundResource(R.color.grey)
            holder.name.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
        }

        holder.itemView.setOnClickListener {
            // Lưu vị trí item được chọn
            if (selectedPosition == RecyclerView.NO_POSITION) {
                selectedPosition = position
            } else {
                if (selectedPosition == position) {
                    selectedPosition = RecyclerView.NO_POSITION // Xóa vị trí đã chọn
                } else {
                    selectedPosition = position
                }
            }


            // Thông báo cho Adapter cần phải cập nhật lại toàn bộ danh sách
            notifyDataSetChanged()

            listener.onClick(itemData, position, selectedPosition)

        }
    }
}
