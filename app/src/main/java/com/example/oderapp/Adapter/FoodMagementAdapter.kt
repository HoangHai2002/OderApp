package com.example.oderapp.Adapter

import android.content.Context
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.oderapp.Activity.FoodManagement
import com.example.oderapp.Model.MonAn
import com.example.oderapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FoodManagementAdapter(val context: Context, val listFood: MutableList<MonAn>)
    : RecyclerView.Adapter<FoodManagementAdapter.FoodManagementHolder>() {

        class FoodManagementHolder(view: View) : ViewHolder(view){
            var foodName = view.findViewById<TextView>(R.id.txtFoodName)
            var price = view.findViewById<TextView>(R.id.txtPrice)
            val menuItem = view.findViewById<ImageView>(R.id.menuItem)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodManagementHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_item_food, null)
        return FoodManagementHolder(view)
    }

    override fun getItemCount(): Int {
        return listFood.size
    }

    override fun onBindViewHolder(holder: FoodManagementHolder, position: Int) {
        val items = listFood[position]
        holder.foodName.setText(items.tenMonAn)
        holder.price.setText(items.gia.toString())
        val myDataBase = FirebaseDatabase.getInstance().getReference("MonAn").child(items.id!!)
        holder.menuItem.setOnClickListener{
            val popupMenu = PopupMenu(context, it)
            popupMenu.inflate(R.menu.menu_item_food)
            popupMenu.setOnMenuItemClickListener { menuId ->
                when(menuId.itemId){
                    R.id.delete -> {
                        val dialog = AlertDialog.Builder(context)
                        dialog.setTitle("Thông báo")
                        dialog.setMessage("Bạn có chắc muốn xóa món ăn này")
                        dialog.setPositiveButton("cancel"){dialog, swich ->
                            dialog.dismiss()
                        }
                        dialog.setPositiveButton("Ok"){dialog, swich ->
                            myDataBase.removeValue().addOnSuccessListener {
                                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show()
                                listFood.clear()
                                getFoodData()
                            }.addOnFailureListener {
                                Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show()
                            }
                        }
                        dialog.show()
                        true
                    }
                    R.id.edit -> {
                        val dialog = LayoutInflater.from(context).inflate(R.layout.dialog_edit_food, null)
                        val alertDialog = AlertDialog.Builder(context).setView(dialog).create()

                        var edtEditFoodName = dialog.findViewById<EditText>(R.id.editFoodName)
                        var edtEditPrice = dialog.findViewById<EditText>(R.id.editFoodPrice)
                        val btnChooseCategory = dialog.findViewById<Button>(R.id.btnEditCategory)
                        val btnEdit = dialog.findViewById<Button>(R.id.btnEditFood)
                        edtEditFoodName.setText(items.tenMonAn)
                        edtEditPrice.setText(items.gia.toString())
                        btnChooseCategory.text = items.tenLoaiMonAn
                        btnChooseCategory.setOnClickListener{ view ->
                            val popupMenu = PopupMenu(context, view)
                            popupMenu.inflate(R.menu.food_category)
                            popupMenu.setOnMenuItemClickListener { item ->
                                when(item.itemId){
                                    R.id.bbq -> {
                                        btnChooseCategory.setText(item.title)
                                        true
                                    }
                                    R.id.hotpot ->{
                                        btnChooseCategory.setText(item.title)
                                        true
                                    }
                                    else -> {
                                        false
                                    }
                                }
                            }
                            popupMenu.show()
                        }
                        btnEdit.setOnClickListener{
                            val update = mapOf<String, Any>(
                                "tenMonAn" to edtEditFoodName.text.toString(),
                                "gia" to edtEditPrice.text.toString().toInt()
                            )
                            myDataBase.updateChildren(update).addOnSuccessListener {
                                Toast.makeText(context, "update thành công", Toast.LENGTH_SHORT).show()
                                listFood.clear()
                                getFoodData()
                                alertDialog.dismiss()

                            }.addOnFailureListener {
                                Toast.makeText(context, "update thất bại", Toast.LENGTH_SHORT).show()
                            }
                        }
                        alertDialog.show()
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            popupMenu.show()
        }
    }
    fun getFoodData(){
        val myDataBase = FirebaseDatabase.getInstance().getReference("MonAn")
        myDataBase.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (i in snapshot.children){
                        var foodData = i.getValue(MonAn::class.java)
                        listFood.add(foodData!!)
                        notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}