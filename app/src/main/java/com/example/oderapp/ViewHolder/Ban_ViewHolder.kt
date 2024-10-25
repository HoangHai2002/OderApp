package com.example.oderapp.ViewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.oderapp.Model.Ban
import com.example.oderapp.R

class Ban_ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {
    var tenBan = itemView.findViewById<TextView>(R.id.tv_name)
    var trangThai = itemView.findViewById<ImageView>(R.id.imv_money)
    var thoiGian = itemView.findViewById<TextView>(R.id.tv_time)
    var slkhach = itemView.findViewById<TextView>(R.id.tv_slkhach)
    var tongTien = itemView.findViewById<TextView>(R.id.tv_total)
    var layout = itemView.findViewById<ConstraintLayout>(R.id.layout_itemban)
}