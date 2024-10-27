package com.example.oderapp.Adapter

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.oderapp.Interface.itf_Click_MonAn
import com.example.oderapp.Model.MonAn
import com.example.oderapp.R
import com.example.oderapp.ViewHolder.MonAn_ViewHolder
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MonAn_Adapter(var listMonAn : MutableList<MonAn>, var listener : itf_Click_MonAn): RecyclerView.Adapter<MonAn_ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonAn_ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_ordermonan, parent , false)
        return MonAn_ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listMonAn.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MonAn_ViewHolder, position: Int) {
        var itemData = listMonAn.get(position)
        var formattedNumber = itemData.gia.toString().replace(Regex("(\\d)(?=(\\d{3})+\$)"), "$1,")

        holder.tenMonAn.setText(itemData.tenMonAn)
        holder.gia.setText(formattedNumber)
        holder.edt_soLuong.setText(itemData.soLuong.toString())

        var count = holder.edt_soLuong.text.toString().toInt()
        if (count == 0){
            holder.imv_cong.visibility = View.GONE
            holder.imv_tru.visibility = View.GONE
            holder.edt_soLuong.visibility = View.GONE
        }else{
            holder.imv_cong.visibility = View.VISIBLE
            holder.imv_tru.visibility = View.VISIBLE
            holder.edt_soLuong.visibility = View.VISIBLE
        }

        holder.itemView.setOnClickListener {
            holder.imv_cong.visibility = View.VISIBLE
            holder.imv_tru.visibility = View.VISIBLE
            holder.edt_soLuong.visibility = View.VISIBLE
            ++count
            setSoluong(
                count, itemData.id.toString(),
                itemData.tenMonAn.toString(),
                itemData.tenLoaiMonAn.toString(),
                itemData.gia.toString().toInt()
            )
            holder.edt_soLuong.setText(count.toString())
            listener.onClickMonAn(itemData, position)
        }
        holder.imv_cong.setOnClickListener {
            ++count
            setSoluong(
                count, itemData.id.toString(),
                itemData.tenMonAn.toString(),
                itemData.tenLoaiMonAn.toString(),
                itemData.gia.toString().toInt()
            )
            holder.edt_soLuong.setText(count.toString())
            listener.onClickCong(itemData, position)
        }
        holder.imv_tru.setOnClickListener {
            if(count > 0){
                count--
            }
            setSoluong(
                count, itemData.id.toString(),
                itemData.tenMonAn.toString(),
                itemData.tenLoaiMonAn.toString(),
                itemData.gia.toString().toInt()
            )
            holder.edt_soLuong.setText(count.toString())
            listener.onClickTru(itemData, position)
        }
    }
    fun setSoluong(count : Int, id : String, tenMonAn : String, tenLoaiMonAn : String, gia : Int){
        var dbRef : DatabaseReference = FirebaseDatabase.getInstance().getReference("MonAn").child(id)
        var monAn = MonAn(id, tenMonAn, tenLoaiMonAn, gia, count)
        dbRef.setValue(monAn).addOnCompleteListener{

        }.addOnFailureListener{

        }
    }
}