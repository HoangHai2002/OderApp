package com.example.oderapp.ViewHolder

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.oderapp.R

class BanSelected_ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    var tv_tenMonAn_banSelected = itemView.findViewById<TextView>(R.id.tv_tenMonAn_banSelected)
    var tv_gia_banSelected = itemView.findViewById<TextView>(R.id.tv_gia_banSelected)
    var imv_option = itemView.findViewById<ImageView>(R.id.imv_option)
    var imv_cong_banSelected = itemView.findViewById<ImageView>(R.id.imv_cong_banSelected)
    var imv_tru_banSelected = itemView.findViewById<ImageView>(R.id.imv_tru_banSelected)
    var edt_soluong_banSelected = itemView.findViewById<EditText>(R.id.edt_soluong_banSelected)

}