package com.example.oderapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oderapp.Interface.itf_Click_ThanhToan
import com.example.oderapp.Model.ThanhToan
import com.example.oderapp.R
import com.example.oderapp.ViewHolder.ThanhToan_ViewHolder

class ThanhToan_Adapter(val listData : MutableList<ThanhToan>, val listener : itf_Click_ThanhToan) : RecyclerView.Adapter<ThanhToan_ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThanhToan_ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_thanhtoan, parent, false)
        return ThanhToan_ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: ThanhToan_ViewHolder, position: Int) {
        var itemData = listData.get(position)
        holder.tv_thoiGian.setText("${itemData.thoiGian} - ${itemData.ngayThang}")
        holder.tv_vitri.setText("${itemData.tenKhuVuc}/${itemData.tenBan}")
        var formattedNumber = itemData.tongTien.toString().replace(Regex("(\\d)(?=(\\d{3})+\$)"), "$1,")
        holder.tv_thanhtien.setText("${formattedNumber}đ")

        holder.itemView.setOnClickListener {
            listener.onClick(itemData, position)
        }
    }
}