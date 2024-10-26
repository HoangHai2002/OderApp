package com.example.oderapp.Activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oderapp.Adapter.CategoryFoodAdapter
import com.example.oderapp.Model.LoaiMonAn
import com.example.oderapp.R
import com.example.oderapp.databinding.ActivityCategoryFoodBinding
import com.google.firebase.database.FirebaseDatabase

class CategoryFood : AppCompatActivity() {
    lateinit var bind: ActivityCategoryFoodBinding
    lateinit var adapter: CategoryFoodAdapter
    lateinit var listCategory: MutableList<LoaiMonAn>
    val dataBase = FirebaseDatabase.getInstance().getReference("LoaiMonAn")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bind = ActivityCategoryFoodBinding.inflate(layoutInflater)
        setContentView(bind.root)

        //Toolbar
        setSupportActionBar(bind.toolbar1)
        supportActionBar?.title = "Quản lý loại món"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        listCategory = mutableListOf()
        adapter = CategoryFoodAdapter(this, listCategory)
        adapter.getCategoryData()
        bind.rcCategoryFood.layoutManager = LinearLayoutManager(this)
        bind.rcCategoryFood.adapter = adapter

        bind.btnAddCategory.setOnClickListener{
            val dialog = layoutInflater.inflate(R.layout.dialog_add_category, null)
            val alertDialog = AlertDialog.Builder(this).setView(dialog).create()

            val edtNameCategory = dialog.findViewById<EditText>(R.id.edtCategoryName)
            val addCategory = dialog.findViewById<Button>(R.id.addCategory)

            addCategory.setOnClickListener{
                val data = dataBase.push()
                val categoryId = data.key
                var loaiMonAn = LoaiMonAn(
                    categoryId,
                    edtNameCategory.text.toString()
                )
                data.setValue(loaiMonAn).addOnSuccessListener {
                    Toast.makeText(this, "Thêm loại món ăn thành công", Toast.LENGTH_SHORT).show()
                    listCategory.clear()
                    adapter.getCategoryData()
                }.addOnFailureListener {
                    Toast.makeText(this, "Thêm Thất bại", Toast.LENGTH_SHORT).show()
                }
            }
            alertDialog.show()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return true
    }
}