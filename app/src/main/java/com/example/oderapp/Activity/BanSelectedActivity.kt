package com.example.oderapp.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oderapp.Adapter.BanSelected_Adapter
import com.example.oderapp.Interface.itf_Click_MonAn
import com.example.oderapp.Model.Ban
import com.example.oderapp.Model.MonAn
import com.example.oderapp.Model.MonAnThanhToan
import com.example.oderapp.Model.Order
import com.example.oderapp.Model.ThanhToan
import com.example.oderapp.R
import com.example.oderapp.databinding.ActivityBanSelectedBinding
import com.example.quanlydiem.Preferences
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class BanSelectedActivity : AppCompatActivity() {
    lateinit var bind : ActivityBanSelectedBinding
    lateinit var dbRef : DatabaseReference
    lateinit var listData : MutableList<MonAn>
    lateinit var adapter : BanSelected_Adapter
    val newMonAnThanhToan : MutableList<MonAnThanhToan> = mutableListOf()
    lateinit var preferences : Preferences
    lateinit var id : String

    var sum : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bind = ActivityBanSelectedBinding.inflate(layoutInflater)
        setContentView(bind.root)

        //toolbar
        setSupportActionBar(bind.toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        preferences = Preferences(this@BanSelectedActivity)

        id = intent.getStringExtra("id").toString()
        getTenBan(id)

        val listener = object : itf_Click_MonAn {
            override fun onClickMonAn(itemData: MonAn, gia : Int, count: Int) {
                setSoLuong(id, count, itemData.id, gia)
            }

            override fun onClickXoa(idMonAn: String?, pos: Int) {
                xoaOrder(id, idMonAn, pos)
            }
        }

        listData = mutableListOf()
        checkBanInOrder(id)
        adapter = BanSelected_Adapter(listData, listener, this@BanSelectedActivity)
        bind.rcDsMonTrongBan.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        bind.rcDsMonTrongBan.adapter = adapter


        bind.btnThanhtoan.setOnClickListener {
            val build = AlertDialog.Builder(this)
            build.setTitle("Xác nhận thanh toán?")
            build.setPositiveButton("OK"){ dialog, which ->
                val thanhtoanRef : DatabaseReference = FirebaseDatabase.getInstance().getReference("ThanhToan")
                val newKey = thanhtoanRef.push().key!!
                val newKey1 = thanhtoanRef.push().key!!
                val monAnThanhToan : MutableList<MonAnThanhToan> = mutableListOf()
                val thanhToan = ThanhToan(newKey, "","", "", 0, monAnThanhToan, "", "")

                CoroutineScope(Dispatchers.Main).launch {
                    thanhtoanRef.child(newKey).setValue(thanhToan)
                    checkIdBanThanhToan(id, sum, newKey)
                    delay(1000)
                    resetBan(id)
                    var intent = Intent(this@BanSelectedActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }
            build.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            build.show()
        }






        bind.btnAdd.setOnClickListener {
            var intent = Intent(this@BanSelectedActivity, OrderActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }

    }

    private fun xoaOrder(idBan: String?, idMonAn: String?, pos : Int) {
        dbRef = FirebaseDatabase.getInstance().getReference("Order")
        dbRef.orderByChild("id_ban").equalTo(idBan).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (it in snapshot.children){
                        var order = it.getValue(Order::class.java)
                        if (order?.id_ban == idBan && order?.id_monAn == idMonAn){
                            truTongTien(idMonAn, idBan, order?.soLuong)
                            val xoaRef = FirebaseDatabase.getInstance().getReference("Order").child(order?.id.toString())
                            xoaRef.removeValue()
                            listData.removeAt(pos)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun truTongTien(idMonAn: String?, idBan: String?, soLuong: Int?) {
        dbRef = FirebaseDatabase.getInstance().getReference("MonAn")
        dbRef.orderByChild("id").equalTo(idMonAn).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for(it in snapshot.children){
                        var newMonAn = it.getValue(MonAn::class.java)
                        var tong = newMonAn?.gia!! * soLuong!!
                        sum = sum - tong
                        bind.tvTongtien.setText(sum.toString())
                        updateTongTien(idBan, sum)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun checkIdBanThanhToan(idBan : String?, sum : Int?, idThanhToan : String) {
        dbRef = FirebaseDatabase.getInstance().getReference("Order")
        dbRef.orderByChild("id_ban").equalTo(idBan).addListenerForSingleValueEvent(object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (it in snapshot.children){
                        val order = it.getValue(Order::class.java)
                        if (order?.id_ban == idBan){
                            getTenBanThanhToan(idBan, idThanhToan)
                            getMonAnThanhToan(order?.id_monAn, order?.soLuong, idThanhToan)

                            dbRef = FirebaseDatabase.getInstance().getReference("Order").child(order?.id.toString())
                            dbRef.removeValue()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun getTenBanThanhToan(idBan: String?, idThanhToan: String) {
        dbRef = FirebaseDatabase.getInstance().getReference("Ban")
        dbRef.orderByChild("id").equalTo(idBan).addListenerForSingleValueEvent(object :
            ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                for (it in snapshot.children){
                    var ban = it.getValue(Ban::class.java)

                    val ttRef = FirebaseDatabase.getInstance().getReference("ThanhToan").child(idThanhToan.toString())
                    val newThanhToanUpdates = HashMap<String, Any>()

                    val zoneId = ZoneId.of("Asia/Ho_Chi_Minh")
                    val localTime = LocalTime.now(zoneId)
                    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
                    val formattedTime = localTime.format(timeFormatter).toString()

                    val localDate = LocalDate.now()
                    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    val formattedDate = localDate.format(dateFormatter)

                    newThanhToanUpdates["tenBan"] = ban?.tenBan.toString()
                    newThanhToanUpdates["tenKhuVuc"] = ban?.tenKhuVuc.toString()
                    newThanhToanUpdates["tongTien"] = ban?.tongtien.toString().toInt()
                    newThanhToanUpdates["nguoiThanhToan"] = "${preferences.getFullName()}"
                    newThanhToanUpdates["thoiGian"] = "$formattedTime"
                    newThanhToanUpdates["ngayThang"] = "$formattedDate"
                    ttRef.updateChildren(newThanhToanUpdates)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun getMonAnThanhToan(idMonan: String?, soLuong : Int?, idThanhToan: String) {
        dbRef = FirebaseDatabase.getInstance().getReference("MonAn")
        dbRef.orderByChild("id").equalTo(idMonan).addListenerForSingleValueEvent(object  : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (it in snapshot.children){
                        val monAn = it.getValue(MonAn::class.java)
                        val tong = (monAn?.gia.toString().toInt()) * (soLuong.toString().toInt())
                        newMonAnThanhToan.add(MonAnThanhToan(monAn?.tenMonAn, monAn?.gia, soLuong,tong ))
                    }
                    val thanhtoanRef = FirebaseDatabase.getInstance().getReference("ThanhToan").child(idThanhToan)
                    thanhtoanRef.child("monAn").setValue(newMonAnThanhToan)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    private fun resetBan(id : String?) {
        dbRef = FirebaseDatabase.getInstance().getReference("Ban").child(id.toString())
        val newBanUpdates = HashMap<String, Any>()
        newBanUpdates["trangThai"] = false
        newBanUpdates["tongtien"] = 0
        dbRef.updateChildren(newBanUpdates)
    }

    private fun setSoLuong(idBan: String?, count: Int, idMonAn : String?, gia : Int) {
        dbRef = FirebaseDatabase.getInstance().getReference("Order")
        dbRef.orderByChild("id_ban").equalTo(idBan).addListenerForSingleValueEvent(object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (it in snapshot.children){
                        val order = it.getValue(Order::class.java)
                        if (order?.id_ban == idBan && order?.id_monAn == idMonAn){
                            var slRef : DatabaseReference = FirebaseDatabase.getInstance().getReference("Order").child(order?.id.toString())
                            val newOrderUpdates = HashMap<String, Any>()
                            newOrderUpdates["soLuong"] = count
                            slRef.updateChildren(newOrderUpdates)
                            sum += gia
                            var formattedNumber = sum.toString().replace(Regex("(\\d)(?=(\\d{3})+\$)"), "$1,")
                            bind.tvTongtien.setText(formattedNumber)
                            updateTongTien(idBan, sum)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

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
                huyBan(id)
            return true
        }

        }
        return true
    }

    private fun huyBan(id: String) {
        dbRef = FirebaseDatabase.getInstance().getReference("Order")
        dbRef.orderByChild("id_ban").equalTo(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for(it in snapshot.children){
                        val order = it.getValue(Order::class.java)
                        val orderRef = FirebaseDatabase.getInstance().getReference("Order").child(order?.id.toString())
                        orderRef.removeValue()
                        resetBan(id)
                        val  intent = Intent(this@BanSelectedActivity, MainActivity::class.java)
                        startActivity(intent)
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
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