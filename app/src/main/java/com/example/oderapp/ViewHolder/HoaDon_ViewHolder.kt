package com.example.oderapp.ViewHolder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.oderapp.R
import org.w3c.dom.Text

class HoaDon_ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    var tv_tenmonan_hoadon = itemView.findViewById<TextView>(R.id.tv_tenmonan_hoadon)
    var tv_gia_hoadon = itemView.findViewById<TextView>(R.id.tv_gia_hoadon)
    var tv_soluong_hoadon = itemView.findViewById<TextView>(R.id.tv_soluong_hoadon)
    var tv_tong_hoadon = itemView.findViewById<TextView>(R.id.tv_tong_hoadon)

}