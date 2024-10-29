package com.example.oderapp.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.oderapp.Interface.itf_Click_MonAn
import com.example.oderapp.Model.MonAn
import com.example.oderapp.Model.Order
import com.example.oderapp.R
import com.example.oderapp.ViewHolder.BanSelected_ViewHolder
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.currentCoroutineContext
import kotlin.coroutines.coroutineContext

class BanSelected_Adapter(var listData : MutableList<MonAn>, var listener : itf_Click_MonAn, var context : Context): RecyclerView.Adapter<BanSelected_ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BanSelected_ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_banselected, parent, false)
        return BanSelected_ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: BanSelected_ViewHolder, position: Int) {
        var itemData = listData.get(position)
        holder.tv_tenMonAn_banSelected.setText(itemData.tenMonAn)

        var formattedNumber = itemData.gia.toString().replace(Regex("(\\d)(?=(\\d{3})+\$)"), "$1,")
        holder.tv_gia_banSelected.setText(formattedNumber)
        holder.edt_soluong_banSelected.setText(itemData.soLuong.toString())

        var count =  holder.edt_soluong_banSelected.text.toString().toInt()

        holder.itemView.setOnClickListener {
            ++count
            holder.edt_soluong_banSelected.setText(count.toString())
            listener.onClickMonAn(itemData, itemData.gia.toString().toInt(), count)
        }
        holder.imv_cong_banSelected.setOnClickListener {
            ++count
            holder.edt_soluong_banSelected.setText(count.toString())
            listener.onClickMonAn(itemData, itemData.gia.toString().toInt(), count)
        }
        holder.imv_tru_banSelected.setOnClickListener {
            if(count > 0){
                count--
            }
            holder.edt_soluong_banSelected.setText(count.toString())
            var giaTru = "-${itemData.gia.toString().toInt()}"
            listener.onClickMonAn(itemData, giaTru.toInt(), count)
        }
        holder.imv_option.setOnClickListener {
            showPopupMenu(holder.imv_option){
                    listener.onClickXoa(itemData.id, position)

            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun showPopupMenu(view: View, callback: (String) -> Unit) {
        val popupMenu = PopupMenu(context, view)
            popupMenu.menu.add("Xóa")
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.title.toString()) {
                "Xóa" -> {
                    val value = "Xóa" // Giá trị tương ứng với mục "Xóa"
                    callback(value) // Gọi hàm callback và truyền giá trị value
                    true
                }
            }
            true
        }
        popupMenu.show()
    }
}