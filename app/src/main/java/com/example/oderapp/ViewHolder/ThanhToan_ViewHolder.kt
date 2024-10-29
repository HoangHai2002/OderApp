package com.example.oderapp.ViewHolder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.oderapp.R

class ThanhToan_ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
    var tv_thoiGian = itemView.findViewById<TextView>(R.id.tv_thoigian)
    var tv_vitri = itemView.findViewById<TextView>(R.id.tv_vitri)
    var tv_thanhtien = itemView.findViewById<TextView>(R.id.tv_thanhtien)
}