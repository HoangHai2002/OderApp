package com.example.oderapp.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oderapp.Adapter.HoaDon_Adapter
import com.example.oderapp.Adapter.ThanhToan_Adapter
import com.example.oderapp.Model.MonAn
import com.example.oderapp.Model.MonAnThanhToan
import com.example.oderapp.Model.ThanhToan
import com.example.oderapp.R
import com.example.oderapp.databinding.ActivityHoaDonBinding
import com.example.oderapp.databinding.ActivityMainBinding
import com.example.quanlydiem.Preferences
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HoaDonActivity : AppCompatActivity() {
    lateinit var bind : ActivityHoaDonBinding
    lateinit var adapter : HoaDon_Adapter
    lateinit var listData : MutableList<MonAnThanhToan>
    lateinit var idThanhToan : String
    lateinit var preferences: Preferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bind = ActivityHoaDonBinding.inflate(layoutInflater)
        setContentView(bind.root)
        preferences = Preferences(this@HoaDonActivity)

        //Toolbar
        setSupportActionBar(bind.toolbar1)
        supportActionBar?.title = "Hóa đơn thanh toán"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        idThanhToan  = intent.getStringExtra("idThanhToan").toString()
        listData = mutableListOf()

        getThanhToan()
        adapter = HoaDon_Adapter(listData)
        bind.rcHoadon.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        bind.rcHoadon.adapter = adapter

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                finish()
                return true
            }
        }
        return true
    }
    private fun getThanhToan() {
        var dbRef = FirebaseDatabase.getInstance().getReference("ThanhToan")
        dbRef.orderByChild("id").equalTo(idThanhToan).addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val thanhToan = dataSnapshot.getValue(ThanhToan::class.java)
                    if (thanhToan != null) {
                        bind.tvMaHoaDon.setText(thanhToan.id)
                        bind.tvViTriNgoi.setText("${thanhToan.tenKhuVuc}/${thanhToan.tenBan}")
                        bind.tvNguoiTHanhToan.setText("${preferences.getFullName()}")
                        bind.tvNgayThanhToan.setText(thanhToan.ngayThang)
                        bind.tvThoiGian.setText(thanhToan.thoiGian)
                        var tongTien = thanhToan.tongTien.toString().replace(Regex("(\\d)(?=(\\d{3})+\$)"), "$1,")
                        bind.tvTong.setText("${tongTien}đ")
                        var list = thanhToan.monAn
                        for (it in list){
                            listData .add(it)
                            adapter.notifyDataSetChanged()
                        }

                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}