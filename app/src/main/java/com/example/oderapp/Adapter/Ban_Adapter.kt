package com.example.oderapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oderapp.Interface.itf_Click_Ban
import com.example.oderapp.Model.Ban
import com.example.oderapp.R
import com.example.oderapp.ViewHolder.Ban_ViewHolder

class Ban_Adapter(private var listBan : MutableList<Ban>, var listener : itf_Click_Ban) : RecyclerView.Adapter<Ban_ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Ban_ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_ban, parent, false)
        return Ban_ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listBan.size
    }

    override fun onBindViewHolder(holder: Ban_ViewHolder, position: Int) {
        var itemData = listBan.get(position)
        holder.tenBan.setText(itemData.tenBan)
        holder.thoiGian.setText(itemData.thoigian)
        holder.slkhach.setText("${itemData.slKhach} khách")

        var tongTien = itemData.tongtien.toString().replace(Regex("(\\d)(?=(\\d{3})+\$)"), "$1,")

        holder.tongTien.setText("${tongTien}đ")

        if (itemData.trangThai == true){
            holder.layout.setBackgroundResource(R.drawable.background_ban_selected)
            holder.tongTien.visibility = View.VISIBLE
        }
        holder.itemView.setOnClickListener {
            listener.onClick(itemData, position)
        }
    }
}