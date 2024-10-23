package com.example.oderapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oderapp.Interface.itf_UD_QLBan
import com.example.oderapp.Model.Ban
import com.example.oderapp.R
import com.example.oderapp.ViewHolder.QlBan_ViewHolder

class QlBan_Adapter(var listData : MutableList<Ban>, private  var listener : itf_UD_QLBan)
    : RecyclerView.Adapter<QlBan_ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QlBan_ViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_qlban, parent, false)
        return QlBan_ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: QlBan_ViewHolder, position: Int) {
        var itemData = listData.get(position)
        holder.tenBan.setText(itemData.tenBan)
        holder.update.setOnClickListener {
            listener.onClickUpdate(itemData, position)
        }
        holder.delete.setOnClickListener {
            listener.onClickDelete(itemData, position)
        }
    }
}
