package com.example.oderapp.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oderapp.Adapter.QlKhuvucban_Adapter
import com.example.oderapp.Interface.itf_UD_QLKVBan
import com.example.oderapp.Model.KhuVucBan
import com.example.oderapp.databinding.ActivityKhuVucBanBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class KhuVucBanActivity : AppCompatActivity() {
    lateinit var bind: ActivityKhuVucBanBinding
    lateinit var adapter: QlKhuvucban_Adapter
    lateinit var listKvban: MutableList<KhuVucBan>
    lateinit var dbRef: DatabaseReference

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bind = ActivityKhuVucBanBinding.inflate(layoutInflater)
        setContentView(bind.root)

        // Update and Delete
        var listener = object : itf_UD_QLKVBan {
            override fun onClickUpdate(itemData: KhuVucBan, pos: Int) {
                dialogUpdate(itemData.id.toString(), itemData.name.toString(), pos)

            }

            override fun onClickDelete(itemData: KhuVucBan, pos: Int) {
                delete(itemData.id.toString(), itemData.name.toString())
            }
        }

        //Binding data
        listKvban = mutableListOf()
        adapter = QlKhuvucban_Adapter(listKvban, listener, this@KhuVucBanActivity)
        bind.rcQlkvban.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        bind.rcQlkvban.adapter = adapter

        getDataKhuVucBan()

        //Toolbar
        setSupportActionBar(bind.toolbar1)
        supportActionBar?.title = "Quản lý khu vực bàn"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bind.imvAdd.setOnClickListener {
            dialogThemKv()
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

    fun xoaKV(id: String, name: String) {
        dbRef = FirebaseDatabase.getInstance().getReference("KhuVucBan").child(id)
        var kq = dbRef.removeValue()
        kq.addOnSuccessListener {
            Toast.makeText(this@KhuVucBanActivity, "Đã xóa ${name}", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this@KhuVucBanActivity, "Đã xóa ${name}", Toast.LENGTH_SHORT).show()
        }
    }

    fun themKV(tenKv: String) {
        dbRef = FirebaseDatabase.getInstance().getReference("KhuVucBan")
        dbRef.orderByChild("name").equalTo(tenKv).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var check: Boolean = true
                for (it in snapshot.children) {
                    var kvban = it.getValue(KhuVucBan::class.java)
                    if (kvban != null && kvban.name == tenKv) {
                        Toast.makeText(
                            this@KhuVucBanActivity,
                            "Khu vực đã tồn tại",
                            Toast.LENGTH_SHORT
                        ).show()
                        check = false
                        return
                    }
                }

                if (check) {
                    var newRef = dbRef.push().key!!
                    var newkv = KhuVucBan(newRef, tenKv)
                    dbRef.child(newRef).setValue(newkv).addOnCompleteListener {
                        listKvban.add(KhuVucBan(newRef, tenKv))
                        adapter.notifyDataSetChanged()
                        Toast.makeText(
                            this@KhuVucBanActivity,
                            "Thêm thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                    }.addOnFailureListener {
                        Toast.makeText(this@KhuVucBanActivity, "Lỗi", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@KhuVucBanActivity, "ok", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    fun suaKV(id: String, newName: String, pos : Int) {
        dbRef = FirebaseDatabase.getInstance().getReference("KhuVucBan")
        dbRef.orderByChild("name").equalTo(newName).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var check: Boolean = true
                for (it in snapshot.children) {
                    var kvban = it.getValue(KhuVucBan::class.java)
                    if (kvban != null && kvban.name == newName) {
                        Toast.makeText(
                            this@KhuVucBanActivity,
                            "Khu vực đã tồn tại",
                            Toast.LENGTH_SHORT
                        ).show()
                        check = false
                        return
                    }
                }
                if(check){
                    dbRef = FirebaseDatabase.getInstance().getReference("KhuVucBan").child(id)
                    var khuVuc = KhuVucBan(id, newName)
                    dbRef.setValue(khuVuc).addOnCompleteListener {
                        listKvban[pos].name = newName
                        adapter.notifyDataSetChanged()
                        Toast.makeText(this@KhuVucBanActivity, "Sửa thành công", Toast.LENGTH_SHORT)
                            .show()
                    }.addOnFailureListener {
                        Toast.makeText(this@KhuVucBanActivity, "Lỗi", Toast.LENGTH_SHORT).show()
                    }
                }
                }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun delete(id : String, name : String){
        val build = AlertDialog.Builder(this)
        build.setTitle("Xác nhận xóa khu $name?")
        build.setPositiveButton("OK"){ dialog, which ->
            xoaKV(id, name)
            listKvban.removeIf { it.id == id }
            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }
        build.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        build.show()
    }
    
    fun dialogUpdate(id: String, name: String, pos : Int) {
        val build = AlertDialog.Builder(this)
        build.setTitle("Sửa khu vực bàn.")
        val inputText = EditText(this)
        inputText.hint = "Nhập tên khu vực"
        inputText.setText(name)
        build.setView(inputText)

        build.setPositiveButton("OK") { dialog, which ->
            val newName = inputText.text.toString()
            if (inputText.text.toString() == "") {
                inputText.error = "Vui lòng nhập tên khu vực"
            } else {
                suaKV(id, newName, pos)
                dialog.dismiss()
            }
        }
        build.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        build.show()
    }

    fun dialogThemKv() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Thêm khu vực bàn")

        // Set up the input
        val input = EditText(this)
        input.hint = "Nhập tên khu vực"
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK") { dialog, which ->
            val userInput = input.text.toString()
            if (userInput.isEmpty()) {
                Toast.makeText(this, "$userInput", Toast.LENGTH_SHORT).show()
                input.error = "Vui lòng nhập tên khu vực"
                return@setPositiveButton
            }else{
                themKV(userInput)
                dialog.dismiss()
            }
            
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun getDataKhuVucBan() {
        dbRef = FirebaseDatabase.getInstance().getReference("KhuVucBan")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (it in snapshot.children) {
                        val kv = it.getValue(KhuVucBan::class.java)
                        listKvban.add(kv!!)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@KhuVucBanActivity, error.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}