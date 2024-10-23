package com.example.oderapp.Activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oderapp.Adapter.LoaiMonAn_Adapter
import com.example.oderapp.Fragment.MonAn_Fragment
import com.example.oderapp.Fragment.Tatca_Fragment
import com.example.oderapp.Interface.itf_Click_LoaiMonAn
import com.example.oderapp.Model.Ban
import com.example.oderapp.Model.KhuVucBan
import com.example.oderapp.Model.LoaiMonAn
import com.example.oderapp.R
import com.example.oderapp.databinding.ActivityOrderBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OrderActivity : AppCompatActivity() {
    lateinit var bind : ActivityOrderBinding
    lateinit var dbRef : DatabaseReference
    lateinit var listLoaiMonAn : MutableList<LoaiMonAn>
    lateinit var adapter : LoaiMonAn_Adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bind = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(bind.root)

        var id = intent.getStringExtra("id")
        getBan_KV(id)

        // Nút X
        bind.imvClose.setOnClickListener {
            finish()
            Toast.makeText(this@OrderActivity, "1${resestSoLuong()}", Toast.LENGTH_SHORT).show()
            resestSoLuong()
        }

        // Nút quay lại
        bind.btnQuaylai.setOnClickListener {
            finish()
            resestSoLuong()
        }

        // Nút Xác nhận
        bind.btnXacnhan.setOnClickListener {

        }


        val listener = object : itf_Click_LoaiMonAn {
            override fun onClick(itemData: LoaiMonAn, pos: Int, posSelected: Int) {
                var tenLoaiMonAn : String? = ""
                if(posSelected != RecyclerView.NO_POSITION){
                    tenLoaiMonAn = itemData.name.toString()
                }else{
                    tenLoaiMonAn = null
                }
                val fragment = MonAn_Fragment()
                val bundle = Bundle()
                bundle.putString("name", tenLoaiMonAn)
                fragment.arguments = bundle
                replaceFragment(fragment)
            }

        }
        listLoaiMonAn = mutableListOf()
        getLoaiMonAn()
        adapter = LoaiMonAn_Adapter(listLoaiMonAn, listener)
        bind.rcLoaiMonAn.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        bind.rcLoaiMonAn.adapter = adapter

        replaceFragment(MonAn_Fragment())
    }

    private fun resestSoLuong(){
        dbRef = FirebaseDatabase.getInstance().getReference("MonAn")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (it in snapshot.children){
                        var key = it.key
                        key?.let {
                            var monAnRef = dbRef.child(it)
                            monAnRef.child("soLuong").setValue(0)
                        }
                    }

                }
               
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


    fun getLoaiMonAn() {
        dbRef = FirebaseDatabase.getInstance().getReference("LoaiMonAn")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (it in snapshot.children) {
                        val loaiMonAn = it.getValue(LoaiMonAn::class.java)
                        listLoaiMonAn.add(loaiMonAn!!)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@OrderActivity, error.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.layout_monAn,fragment).commit()
    }

    private fun getBan_KV(id: String?) {
        dbRef = FirebaseDatabase.getInstance().getReference("Ban")
        dbRef.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               for (it in snapshot.children){
                   var ban = it.getValue(Ban::class.java)
                   bind.tvTenKvban.setText("${ban?.tenBan}/${ban?.tenKhuVuc}")
               }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}