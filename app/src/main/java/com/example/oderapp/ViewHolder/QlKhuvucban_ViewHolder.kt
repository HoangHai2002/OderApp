package com.example.oderapp.ViewHolder

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.oderapp.R

class QlKhuvucban_ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    var tenkv = itemView.findViewById<TextView>(R.id.tenkv)
    var update = itemView.findViewById<ImageView>(R.id.imv_update)
    var delete = itemView.findViewById<ImageView>(R.id.imv_delete)
}