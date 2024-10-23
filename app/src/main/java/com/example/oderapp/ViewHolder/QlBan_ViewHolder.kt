package com.example.oderapp.ViewHolder

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.oderapp.R

class QlBan_ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    var tenBan = itemView.findViewById<TextView>(R.id.tenban)
    var update = itemView.findViewById<ImageView>(R.id.imv_update_qlban)
    var delete = itemView.findViewById<ImageView>(R.id.imv_delete_qlban)
}