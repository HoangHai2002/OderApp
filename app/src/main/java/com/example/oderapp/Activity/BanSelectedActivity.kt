package com.example.oderapp.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oderapp.Adapter.BanSelected_Adapter
import com.example.oderapp.Model.Ban
import com.example.oderapp.Model.MonAn
import com.example.oderapp.Model.Order
import com.example.oderapp.R
import com.example.oderapp.databinding.ActivityBanSelectedBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BanSelectedActivity : AppCompatActivity() {
    lateinit var bind : ActivityBanSelectedBinding
    lateinit var dbRef : DatabaseReference
    lateinit var listData : MutableList<MonAn>
    lateinit var adapter : BanSelected_Adapter

    var sum : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bind = ActivityBanSelectedBinding.inflate(layoutInflater)
        setContentView(bind.root)

        setSupportActionBar(bind.toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var id = intent.getStringExtra("id")
        getTenBan(id)
        listData = mutableListOf()
        checkBanInOrder(id)
        adapter = BanSelected_Adapter(listData)
        bind.rcDsMonTrongBan.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        bind.rcDsMonTrongBan.adapter = adapter


        bind.btnThanhtoan.setOnClickListener {
            dbRef = FirebaseDatabase.getInstance().getReference("Ban").child(id.toString())
            val newBanUpdates = HashMap<String, Any>()
            newBanUpdates["trangThai"] = false
            dbRef.updateChildren(newBanUpdates)
        }
        bind.btnAdd.setOnClickListener {
            var intent = Intent(this@BanSelectedActivity, OrderActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }

    }

    private fun checkBanInOrder(idBan : String?) {
        dbRef = FirebaseDatabase.getInstance().getReference("Order")
        dbRef.orderByChild("id_ban").equalTo(idBan).addListenerForSingleValueEvent(object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (it in snapshot.children){
                        val order = it.getValue(Order::class.java)
                        if (order?.id_ban == idBan){
                            getMonAnInOrder(order?.id_monAn, order?.soLuong, idBan)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun getMonAnInOrder(idMonan: String?, soLuong : Int?, idBan: String?) {
        dbRef = FirebaseDatabase.getInstance().getReference("MonAn")
        dbRef.orderByChild("id").equalTo(idMonan).addListenerForSingleValueEvent(object  : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (it in snapshot.children){
                        val monAn = it.getValue(MonAn::class.java)
                        listData.add(MonAn(monAn?.id, monAn?.tenMonAn, monAn?.tenLoaiMonAn, monAn?.gia, soLuong))
                        adapter.notifyDataSetChanged()
                        sum += (monAn?.gia.toString().toInt()) * soLuong!!
                        updateTongTien(idBan, sum)
                        var formattedNumber = sum.toString().replace(Regex("(\\d)(?=(\\d{3})+\$)"), "$1,")
                        bind.tvTongtien.setText(formattedNumber)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun updateTongTien(idBan: String?, sum: Int) {
        dbRef = FirebaseDatabase.getInstance().getReference("Ban").child(idBan.toString())
        val newBanUpdates = HashMap<String, Any>()
        newBanUpdates["tongtien"] = sum
        dbRef.updateChildren(newBanUpdates)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                finish()
                var intent = Intent(this, MainActivity::class.java)
                intent.putExtra("tongtien", sum)
                startActivity(intent)
                return true
            }
            R.id.btn_item1 ->{
                Toast.makeText(this, "Item1", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.btn_item2 ->{
                Toast.makeText(this, "Item2", Toast.LENGTH_SHORT).show()
            return true
        }

        }
        return true
    }

    private fun getTenBan(id: String?) {
        dbRef = FirebaseDatabase.getInstance().getReference("Ban")
        dbRef.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (it in snapshot.children){
                    var ban = it.getValue(Ban::class.java)
                    supportActionBar?.title = "${ban?.tenBan}"
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_banselected, menu)
        return true
    }

}