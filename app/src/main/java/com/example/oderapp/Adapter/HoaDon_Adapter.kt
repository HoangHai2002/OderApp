package com.example.oderapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oderapp.Model.MonAnThanhToan
import com.example.oderapp.R
import com.example.oderapp.ViewHolder.HoaDon_ViewHolder

class HoaDon_Adapter(val listData : MutableList<MonAnThanhToan>) : RecyclerView.Adapter<HoaDon_ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoaDon_ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hoadon, parent, false)
        return HoaDon_ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: HoaDon_ViewHolder, position: Int) {
        var itemData = listData.get(position)
        holder.tv_tenmonan_hoadon.setText(itemData.tenMon)
        var gia = itemData.gia.toString().replace(Regex("(\\d)(?=(\\d{3})+\$)"), "$1,")
        holder.tv_gia_hoadon.setText(gia)
        holder.tv_soluong_hoadon.setText(itemData.soLuong.toString())
        var tong = itemData.tong.toString().replace(Regex("(\\d)(?=(\\d{3})+\$)"), "$1,")
        holder.tv_tong_hoadon.setText(tong)
    }
}