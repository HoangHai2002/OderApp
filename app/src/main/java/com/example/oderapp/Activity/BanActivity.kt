package com.example.oderapp.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oderapp.Adapter.Khuvucban_Adapter
import com.example.oderapp.Adapter.QlBan_Adapter
import com.example.oderapp.Fragment.QlyBan_Fragment
import com.example.oderapp.Interface.itf_Click_khuVucBan
import com.example.oderapp.Model.Ban
import com.example.oderapp.Model.KhuVucBan
import com.example.oderapp.R
import com.example.oderapp.databinding.ActivityBanBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BanActivity : AppCompatActivity() {
    lateinit var bind: ActivityBanBinding
    lateinit var adapterBan: QlBan_Adapter
    lateinit var adapterKV: Khuvucban_Adapter
    lateinit var listBan: MutableList<Ban>
    lateinit var listKvBan: MutableList<KhuVucBan>
    lateinit var dbRef: DatabaseReference
    var name: String = "triệu đức Hiếu"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bind = ActivityBanBinding.inflate(layoutInflater)
        setContentView(bind.root)

        setSupportActionBar(bind.toolbar1)
        supportActionBar?.title = "Quản lý bàn"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Hiển thi bàn tương úng với khu vực được chon
        var listener_khuVucBan = object :itf_Click_khuVucBan{
            override fun onClick(itemData: KhuVucBan, pos: Int, posSelected: Int) {
                var tenkv : String? = ""
                if(posSelected != RecyclerView.NO_POSITION){
                    tenkv = itemData.name.toString()
                }else{
                    tenkv = null
                }
                var fragment = QlyBan_Fragment()
                val bundle = Bundle()
                bundle.putString("a", tenkv)
                fragment.arguments = bundle
                replaceFragment(fragment)
            }
        }
        //Khu vực bàn
        listKvBan = mutableListOf()
        getDataKhuVucBan()
        adapterKV = Khuvucban_Adapter(listKvBan, listener_khuVucBan)
        bind.rcKvban.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        bind.rcKvban.adapter = adapterKV



        //Thêm bàn
        bind.imvAdd.setOnClickListener {
            dialogThemBan()
        }



        //Hiển thị bàn
        replaceFragment(QlyBan_Fragment())
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.rc_ban_qlBan, fragment).commit()
    }

    fun getDataKhuVucBan() {
        dbRef = FirebaseDatabase.getInstance().getReference("KhuVucBan")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (it in snapshot.children) {
                        val kv = it.getValue(KhuVucBan::class.java)
                        listKvBan.add(kv!!)
                        adapterKV.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@BanActivity, error.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
    fun dialogThemBan() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Thêm bàn")

//        showPopupMenu(tv)
        // Set up the input
        val tenBan = EditText(this)
        tenBan.hint = "Nhập tên bàn"


        val khuVuc = TextView(this)
        khuVuc.hint = "Chọn khu vực"
        khuVuc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)



        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.addView(khuVuc)
        layout.addView(tenBan)

        khuVuc.setOnClickListener {
            showPopupMenu(khuVuc){ a ->
                khuVuc.setText(a)
            }
        }
        builder.setView(layout)
        if (tenBan.text.toString() == "") {
            tenBan.error = "Vui lòng nhập tên bàn"
        }

        // Set up the buttons
        builder.setPositiveButton("OK") { dialog, which ->
            if (khuVuc.text.toString() == ""){
                Toast.makeText(this@BanActivity, "Khu vực không được để trống", Toast.LENGTH_SHORT).show()
            }else if (tenBan.text.toString() == "") {
                Toast.makeText(this@BanActivity, "Tên bàn không được để trống", Toast.LENGTH_SHORT).show()
            } else {
                themKV(khuVuc.text.toString(), tenBan.text.toString())
                dialog.dismiss()
            }

        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }
    fun showPopupMenu(view: View,  callback: (String) -> Unit) {
        val popupMenu = PopupMenu(this, view)
        for (item in listKvBan){
            popupMenu.menu.add(item.name)
        }
        var a: String = ""
        popupMenu.setOnMenuItemClickListener { item ->

            when (item.title) {
                in listKvBan.map { it.name } -> {
                    val selectedKv = listKvBan.find { it.name == item.title.toString() }
                    a = selectedKv?.name ?: ""
                    callback(a)
                    true
                }
                else -> false
            }
            true
        }
        popupMenu.show()
    }
    fun themKV(tenKv: String, tenBan: String) {
        dbRef = FirebaseDatabase.getInstance().getReference("Ban")
        dbRef.orderByChild("tenBan").equalTo(tenBan).addListenerForSingleValueEvent(object :
            ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                var check: Boolean = true
                for (it in snapshot.children) {
                    var ban = it.getValue(Ban::class.java)
                    if (ban != null && ban.tenBan == tenBan) {
                        Toast.makeText(
                            this@BanActivity,
                            "Bàn đã tồn tại",
                            Toast.LENGTH_SHORT
                        ).show()
                        check = false
                        return
                    }
                }

                if (check) {
                    var newRef = dbRef.push().key!!
                    var newBan = Ban(newRef, tenKv, tenBan, false)
                    dbRef.child(newRef).setValue(newBan).addOnCompleteListener {
                        Toast.makeText(
                            this@BanActivity,
                            "Thêm thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                        replaceFragment(QlyBan_Fragment())

                    }.addOnFailureListener {
                        Toast.makeText(this@BanActivity, "Lỗi", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@BanActivity, "Lỗi", Toast.LENGTH_SHORT).show()
            }
        })
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