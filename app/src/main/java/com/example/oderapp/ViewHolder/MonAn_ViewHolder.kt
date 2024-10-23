package com.example.oderapp.ViewHolder

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.oderapp.R

class MonAn_ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    var tenMonAn = itemView.findViewById<TextView>(R.id.tv_tenMonAn)
    var gia = itemView.findViewById<TextView>(R.id.tv_gia)
    var imv_cong = itemView.findViewById<ImageView>(R.id.imv_cong)
    var imv_tru = itemView.findViewById<ImageView>(R.id.imv_tru)
    var edt_soLuong = itemView.findViewById<EditText>(R.id.edt_soluong)
}