package com.example.oderapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.oderapp.Interface.itf_UD_QLBan
import com.example.oderapp.Model.Ban
import com.example.oderapp.R
import com.example.oderapp.ViewHolder.QlBan_ViewHolder

class QlBan_Adapter(var listData : MutableList<Ban>, private  var listener : itf_UD_QLBan, var context : Context)
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
        var key = ""
        holder.tenBan.setText(itemData.tenBan)
        holder.imvOptionQLB.setOnClickListener {
            showPopupMenu(holder.imvOptionQLB){
                key = it
                if (it.equals("Xóa bàn")){
                    listener.onClickDelete(itemData, position)
                }
                if (it.equals("Sửa bàn")){
                    listener.onClickUpdate(itemData, position)
                }

            }
        }

    }
    fun showPopupMenu(view: View, callback: (String) -> Unit) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.menu.add("Sửa bàn")
        popupMenu.menu.add("Xóa bàn")
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.title.toString()) {
                "Xóa bàn" -> {
                    val value = "Xóa bàn" // Giá trị tương ứng với mục "Xóa"
                    callback(value) // Gọi hàm callback và truyền giá trị value
                    true
                }
                "Sửa bàn" -> {
                    val value = "Sửa bàn" // Giá trị tương ứng với mục "Xóa"
                    callback(value) // Gọi hàm callback và truyền giá trị value
                    true
                }
            }
            true
        }
        popupMenu.show()
    }
}
