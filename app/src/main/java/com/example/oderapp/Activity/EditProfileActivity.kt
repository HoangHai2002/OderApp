package com.example.oderapp.Activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.oderapp.Adapter.QlKhuvucban_Adapter
import com.example.oderapp.Model.KhuVucBan
import com.example.oderapp.Model.User
import com.example.oderapp.R
import com.example.oderapp.databinding.ActivityEditProfileBinding
import com.example.oderapp.databinding.ActivityKhuVucBanBinding
import com.example.quanlydiem.Preferences
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditProfileActivity : AppCompatActivity() {
    lateinit var bind: ActivityEditProfileBinding
    lateinit var dbRef: DatabaseReference
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bind = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(bind.root)

        //toolbar
        setSupportActionBar(bind.toolbar1)
        supportActionBar?.title = "Thông tin tài khoản"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val quanlyRef = FirebaseDatabase.getInstance().getReference("QuanLy")
        val nhanvienRef = FirebaseDatabase.getInstance().getReference("NhanVien")
        preferences = Preferences(this@EditProfileActivity)
        if (preferences.isAdmin()){
            getInfor(quanlyRef, preferences.getFullName())
        }else{
            getInfor(nhanvienRef, preferences.getFullName())
        }

        bind.btnXacNhan.setOnClickListener {

            if (bind.edtFullName.text.toString() == ""){
                bind.edtFullName.error = "Tên người dùng không được để trống"
            }else{
                if  (preferences.isAdmin()){
                    setUser(bind.edtFullName.text.toString(),bind.tvUsername.text.toString(), quanlyRef)
                }else{
                    setUser(bind.edtFullName.text.toString(),bind.tvUsername.text.toString(), nhanvienRef)
                }

            }
        }
    }

    private fun setUser(fullName: String, userName : String, dbRef : DatabaseReference) {
        dbRef.orderByChild("username").equalTo(userName).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (it in snapshot.children){
                        val user = it.getValue(User::class.java)
                        val userRef = dbRef.child(user?.id.toString())
                        var newUserUpdate = HashMap<String, Any>()
                        newUserUpdate["fullName"] = fullName
                        userRef.updateChildren(newUserUpdate)
                        preferences.setFullName(bind.edtFullName.text.toString())
                        Toast.makeText(this@EditProfileActivity, "Sửa thông tin thành công", Toast.LENGTH_SHORT).show()
                        finish()
                        var intent = Intent(this@EditProfileActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getInfor(dbRef: DatabaseReference, fullName : String?) {
        dbRef.orderByChild("fullName").equalTo(fullName).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (it in snapshot.children){
                        val user = it.getValue(User::class.java)
                        bind.tvUsername.setText(user?.username)
                        bind.tvPassword.setText(user?.password)
                        bind.edtFullName.setText(user?.fullName)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println(error.message)
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