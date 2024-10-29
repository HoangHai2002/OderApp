package com.example.oderapp.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oderapp.Adapter.ThanhToan_Adapter
import com.example.oderapp.Interface.itf_Click_ThanhToan
import com.example.oderapp.Model.MonAn
import com.example.oderapp.Model.MonAnThanhToan
import com.example.oderapp.Model.ThanhToan
import com.example.oderapp.R
import com.example.oderapp.databinding.ActivityThanhToanBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ThanhToanActivity : AppCompatActivity() {
    lateinit var bind : ActivityThanhToanBinding
    lateinit var adapter : ThanhToan_Adapter
    lateinit var listData : MutableList<ThanhToan>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bind = ActivityThanhToanBinding.inflate(layoutInflater)
        setContentView(bind.root)




        //Toolbar
        setSupportActionBar(bind.toolbar1)
        supportActionBar?.title = "Lịch sử thanh toán"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var listener = object : itf_Click_ThanhToan{
            override fun onClick(itemData: ThanhToan, pos: Int) {
                val intent = Intent(this@ThanhToanActivity, HoaDonActivity::class.java)
                intent.putExtra("idThanhToan", "${itemData.id}")
                startActivity(intent)
            }
        }

        listData = mutableListOf()

        getThanhToan()
        adapter = ThanhToan_Adapter(listData, listener)
        bind.rcThanhtoan.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        bind.rcThanhtoan.adapter = adapter
    }

    private fun getThanhToan() {
        var dbRef = FirebaseDatabase.getInstance().getReference("ThanhToan")
        dbRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
            override fun onDataChange(snapshot: DataSnapshot) {
                listData.clear()
                for (dataSnapshot in snapshot.children) {
                    val thanhToan = dataSnapshot.getValue(ThanhToan::class.java)
                        listData.add(thanhToan!!)
                        adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
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
}