package com.example.oderapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oderapp.Interface.itf_UD_QLKVBan
import com.example.oderapp.Model.KhuVucBan
import com.example.oderapp.R
import com.example.oderapp.ViewHolder.QlKhuvucban_ViewHolder

class QlKhuvucban_Adapter(var listData : MutableList<KhuVucBan>, private  var listener : itf_UD_QLKVBan)
    : RecyclerView.Adapter<QlKhuvucban_ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QlKhuvucban_ViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_qlkvban, parent, false)
        return QlKhuvucban_ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: QlKhuvucban_ViewHolder, position: Int) {
        var itemData = listData.get(position)
        holder.tenkv.setText(itemData.name)
        holder.update.setOnClickListener {
            listener.onClickUpdate(itemData, position)
        }
        holder.delete.setOnClickListener {
            listener.onClickDelete(itemData, position)
        }
    }
}
