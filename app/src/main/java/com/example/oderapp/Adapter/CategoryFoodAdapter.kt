package com.example.oderapp.Adapter

import android.content.Context
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
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.oderapp.Model.LoaiMonAn
import com.example.oderapp.Model.MonAn
import com.example.oderapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CategoryFoodAdapter(val context: Context ,var list: MutableList<LoaiMonAn>) : RecyclerView.Adapter<CategoryFoodAdapter.CategoryFoodHolder>(){
    class CategoryFoodHolder(view: View) : ViewHolder(view){
        var categoryName = view.findViewById<TextView>(R.id.categoryName)
        val menuItem = view.findViewById<ImageView>(R.id.menuItemCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryFoodHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_category_food, null)
        return CategoryFoodHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CategoryFoodHolder, position: Int) {
        val items = list[position]
        holder.categoryName.setText(items.name)
        val myDataBase = FirebaseDatabase.getInstance().getReference("LoaiMonAn").child(items.id!!)
        holder.menuItem.setOnClickListener{
            val popupMenu = PopupMenu(context, it)
            popupMenu.inflate(R.menu.menu_item_food)
            popupMenu.setOnMenuItemClickListener {idMenu->
                when(idMenu.itemId){
                    R.id.delete ->{
                        val dialog = AlertDialog.Builder(context)
                        dialog.setTitle("Thông báo")
                        dialog.setMessage("Bạn có chắc muốn xóa món ăn này")
                        dialog.setPositiveButton("cancel"){dialog, swich ->
                            dialog.dismiss()
                        }
                        dialog.setPositiveButton("Ok"){dialog, swich ->
                            myDataBase.removeValue().addOnSuccessListener {
                                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show()
                                list.clear()
                                getCategoryData()
                            }.addOnFailureListener {
                                Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show()
                            }
                        }
                        dialog.show()
                        true
                    }
                    R.id.edit -> {
                        val dialog = LayoutInflater.from(context).inflate(R.layout.dialog_edit_category, null)
                        val alertDialog = AlertDialog.Builder(context).setView(dialog).create()

                        val edtEditCategoryFood = dialog.findViewById<EditText>(R.id.edtEditCategoryFood)
                        val btnEditCategoryFood = dialog.findViewById<Button>(R.id.btnEditCategory)
                        edtEditCategoryFood.setText(items.name)
                        btnEditCategoryFood.setOnClickListener{
                            var toMap = mapOf<String, Any>(
                                "name" to edtEditCategoryFood.text.toString()
                            )
                            if (edtEditCategoryFood.text.toString().isNotEmpty()){
                                myDataBase.updateChildren(toMap).addOnSuccessListener {
                                    Toast.makeText(context, "cập nhật thành công", Toast.LENGTH_SHORT).show()
                                    list.clear()
                                    getCategoryData()
                                    alertDialog.dismiss()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "cập nhật thất bại", Toast.LENGTH_SHORT).show()
                                }
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
    fun getCategoryData(){
        val myDataBase = FirebaseDatabase.getInstance().getReference("LoaiMonAn")
        myDataBase.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (i in snapshot.children){
                        var foodData = i.getValue(LoaiMonAn::class.java)
                        list.add(foodData!!)
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