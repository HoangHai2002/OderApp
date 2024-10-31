package com.example.oderapp.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.oderapp.Model.User
import com.example.oderapp.databinding.ActivityLoginBinding
import com.example.quanlydiem.Preferences
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Login : AppCompatActivity() {
    lateinit var bind : ActivityLoginBinding
    private lateinit var userRef : DatabaseReference
    lateinit var firebase : FirebaseDatabase
    lateinit var user : User
    lateinit var preferences : Preferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bind = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bind.root)
        firebase = FirebaseDatabase.getInstance()
        preferences = Preferences(this)
        if (preferences.islogin()){
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        bind.btnLoginLogin.setOnClickListener {
            if (bind.edtUsernameLogin.text.toString() != "" && bind.edtPasswordLogin.text.toString() != ""){
                if (bind.rbtnNhanvien.isChecked){
                    userRef = firebase.getReference("NhanVien")
                    val username = bind.edtUsernameLogin.text.toString().trim()
                    val password = bind.edtPasswordLogin.text.toString().trim()
                    checkUser(username, password, userRef, false)
                }
                if (bind.rbtnQuanly.isChecked){
                    userRef = firebase.getReference("QuanLy")
                    val username = bind.edtUsernameLogin.text.toString().trim()
                    val password = bind.edtPasswordLogin.text.toString().trim()
                    checkUser(username, password, userRef, true)
                }
            }else{
                if (bind.edtUsernameLogin.text.toString() == ""){
                    bind.edtUsernameLogin.error = "Vui lòng nhập tên đăng nhập."
                }
                if (bind.edtPasswordLogin.text.toString() == ""){
                    bind.edtPasswordLogin.error = "Vui lòng nhập mật khẩu."
                }
            }
        }

        bind.tvRedirectRegisterLogin.setOnClickListener {
            var intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

    }
    fun checkUser(username : String, password : String, userRef : DatabaseReference, isAdmin : Boolean){
        userRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null && user.username == username && user.password == password) {
                        // Đăng nhập thành công
                        Toast.makeText(applicationContext, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                        preferences.savelogin(user.fullName!!, isAdmin)
                        var intent = Intent(this@Login, MainActivity::class.java)
                        startActivity(intent)
                        return
                    }
                }
                // Không tìm thấy người dùng hoặc thông tin đăng nhập không chính xác
                Toast.makeText(applicationContext, "Sai thông tin đăng nhập", Toast.LENGTH_SHORT).show()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Xử lý khi truy vấn không thành công
                Toast.makeText(applicationContext, "Lỗi khi truy vấn dữ liệu: " + databaseError.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

}