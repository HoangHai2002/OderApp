package com.example.oderapp.Activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oderapp.Adapter.Khuvucban_Adapter
import com.example.oderapp.Fragment.Sudung_Fragment
import com.example.oderapp.Fragment.Tatca_Fragment
import com.example.oderapp.Interface.itf_Click_khuVucBan
import com.example.oderapp.Interface.itf_UD_QLKVBan
import com.example.oderapp.Model.KhuVucBan
import com.example.oderapp.R
import com.example.oderapp.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    lateinit var bind: ActivityMainBinding
    lateinit var adapter: Khuvucban_Adapter
    lateinit var listData: MutableList<KhuVucBan>
    lateinit var dbRef: DatabaseReference
    lateinit var toggle: ActionBarDrawerToggle
    var posselected : Int = 0
    var tenkv : String? = null
    var trangThaiBan  = "tatca"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        setSupportActionBar(bind.toolbar)
        bind.toolbar.title = ""
        toggle = ActionBarDrawerToggle(
            this,
            bind.drawerLayout,
            bind.toolbar,
            R.string.open,
            R.string.close
        )
        bind.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        // bắt sự kiện click draw
        bind.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.btn_logout -> {
                    Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.btn_qlKhuVucBan -> {
                    var intent = Intent(this@MainActivity, KhuVucBanActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.btn_quanLyBan -> {
                    var intent = Intent(this@MainActivity, BanActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.btn_back2 -> {
                    finish()
                    var intent = Intent(this@MainActivity, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.quanLyMonAn ->{
                    val intent = Intent(this, FoodManagement::class.java)
                    startActivity(intent)
                }
                R.id.categoryManagement ->{
                    val intent = Intent(this, CategoryFood::class.java)
                    startActivity(intent)
                }
            }
            true
        }

        // Gửi tên khu vực qua Fragment để hiển thị bàn tương ứng với khu vực
        val listener = object :itf_Click_khuVucBan{
            override fun onClick(itemData: KhuVucBan, pos: Int, posSelected: Int) {
                posselected = posSelected
                if(posselected != RecyclerView.NO_POSITION){
                    tenkv = itemData.name.toString()
                }else{
                    tenkv = null
                }
                val fragment = Tatca_Fragment()
                val bundle = Bundle()
                bundle.putString("a", tenkv)
                bundle.putString("trangThaiBan", trangThaiBan)
                fragment.arguments = bundle
                replaceFragment(fragment)
            }

        }

        listData = mutableListOf()

        //lấy dữ liệu từ kvban add vào listData
        getDataKhuVucBan()
        adapter = Khuvucban_Adapter(listData, listener)
        bind.rcKhuvucban.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        bind.rcKhuvucban.adapter = adapter

        //Chạy Fragment
        replaceFragment(Tatca_Fragment())

        //Hiện thanh line
        bind.layoutTatca.setOnClickListener {
            trangThaiBan = "tatca"
            bind.line1.visibility = View.VISIBLE
            bind.line2.visibility = View.GONE
            bind.line3.visibility = View.GONE

            val fragment = Tatca_Fragment()
            val bundle = Bundle()
            bundle.putString("a", tenkv)
            bundle.putString("trangThaiBan", trangThaiBan)
            fragment.arguments = bundle
            replaceFragment(fragment)
        }
        bind.layoutSudung.setOnClickListener {
            trangThaiBan = "sudung"
            bind.line1.visibility = View.GONE
            bind.line2.visibility = View.VISIBLE
            bind.line3.visibility = View.GONE

            val fragment = Tatca_Fragment()
            val bundle = Bundle()
            bundle.putString("a", tenkv)
            bundle.putString("trangThaiBan", trangThaiBan)
            fragment.arguments = bundle
            replaceFragment(fragment)
        }
        bind.layoutControng.setOnClickListener {
            trangThaiBan = "controng"
            bind.line1.visibility = View.GONE
            bind.line2.visibility = View.GONE
            bind.line3.visibility = View.VISIBLE

            val fragment = Tatca_Fragment()
            val bundle = Bundle()
            bundle.putString("a", tenkv)
            bundle.putString("trangThaiBan", trangThaiBan)
            fragment.arguments = bundle
            replaceFragment(fragment)
        }

    }

    private fun themKV(tenKv: String) {
        dbRef = FirebaseDatabase.getInstance().getReference("KhuVucBan")
        dbRef.orderByChild("name").equalTo(tenKv).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var check: Boolean = true
                for (it in snapshot.children) {
                    var kvban = it.getValue(KhuVucBan::class.java)
                    if (kvban != null && kvban.name == tenKv) {
                        Toast.makeText(this@MainActivity, "Khu vực đã tồn tại", Toast.LENGTH_SHORT).show()
                        check = false
                        return
                    }
                }

                if (check){
                    // Tạo một nút con mới bằng cách sử dụng push() để tạo id ngẫu nhiên cho người dùng
                    val newRef = dbRef.push().key!!

                    //Tạo đối tượng
                    var newKv = KhuVucBan(newRef, tenKv)

                    //truy cập đến id vừa tạo và đặt giá trị = đối tượng user1
                    dbRef.child(newRef).setValue(newKv).addOnCompleteListener {
                        Toast.makeText(this@MainActivity, "Thêm thành công", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(this@MainActivity, "Thêm không thành công", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun dialogThemKv() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Thêm khu vực bàn")

        // Set up the input
        val input = EditText(this@MainActivity)
        input.hint = "Nhập tên khu vực"
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK") { dialog, which ->
            val userInput = input.text.toString()
            if (userInput != "") {
                themKV(userInput)
                dialog.dismiss()
            }

        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_topbar, menu)
        return true
    }

    // Bat sự kiện thanh toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_search -> {
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.btn_alert -> {
                Toast.makeText(this, "Alert", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

     fun getDataKhuVucBan() {
        dbRef = FirebaseDatabase.getInstance().getReference("KhuVucBan")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (it in snapshot.children) {
                        val kv = it.getValue(KhuVucBan::class.java)
                        listData.add(kv!!)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, error.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.rc_ban, fragment).commit()
    }


}