package com.example.oderapp.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oderapp.Model.MonAn
import com.example.oderapp.Model.Order
import com.example.oderapp.R
import com.example.oderapp.ViewHolder.BanSelected_ViewHolder

class BanSelected_Adapter(var listData : MutableList<MonAn>): RecyclerView.Adapter<BanSelected_ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BanSelected_ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_banselected, parent, false)
        return BanSelected_ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: BanSelected_ViewHolder, position: Int) {
        var itemData = listData.get(position)
        holder.tv_tenMonAn_banSelected.setText(itemData.tenMonAn)
        holder.tv_gia_banSelected.setText(itemData.gia.toString())
        holder.edt_soluong_banSelected.setText(itemData.soLuong.toString())
    }
}