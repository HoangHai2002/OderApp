package com.example.oderapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.oderapp.Interface.itf_UD_QLKVBan
import com.example.oderapp.Model.KhuVucBan
import com.example.oderapp.R
import com.example.oderapp.ViewHolder.QlKhuvucban_ViewHolder

class QlKhuvucban_Adapter(var listData : MutableList<KhuVucBan>, private  var listener : itf_UD_QLKVBan, var context : Context)
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
        var key = ""
        holder.tenkv.setText(itemData.name)
        holder.option.setOnClickListener {
            showPopupMenu(holder.option){
                key = it
                if (it.equals("Xóa khu vực")){
                    listener.onClickDelete(itemData, position)
                }
                if (it.equals("Sửa khu vực")){
                    listener.onClickUpdate(itemData, position)
                }

            }
        }
        
    }

    fun showPopupMenu(view: View, callback: (String) -> Unit) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.menu.add("Sửa khu vực")
        popupMenu.menu.add("Xóa khu vực")
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.title.toString()) {
                "Xóa khu vực" -> {
                    val value = "Xóa khu vực" // Giá trị tương ứng với mục "Xóa"
                    callback(value) // Gọi hàm callback và truyền giá trị value
                    true
                }
                "Sửa khu vực" -> {
                    val value = "Sửa khu vực" // Giá trị tương ứng với mục "Xóa"
                    callback(value) // Gọi hàm callback và truyền giá trị value
                    true
                }
            }
            true
        }
        popupMenu.show()
    }
}
