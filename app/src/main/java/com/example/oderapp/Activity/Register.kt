package com.example.oderapp.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.oderapp.R
import com.example.oderapp.Model.User
import com.example.oderapp.databinding.ActivityRegisterBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Register : AppCompatActivity() {
    lateinit var userRef : DatabaseReference
    lateinit var firebase : FirebaseDatabase
    lateinit var bind : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bind = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(bind.root)
        firebase = FirebaseDatabase.getInstance()

        bind.tvRedirectLoginRegister.setOnClickListener {
            finish()
        }
        bind.btnRegisterRegister.setOnClickListener {
            var username = bind.edtUsernameRegister.text.toString()
            var password = bind.edtPasswordRegister.text.toString()
            var fullName = bind.edtFullName.text.toString()
            var confirmpassword = bind.edtConfirmPasswordRegister.text.toString()

            if(username != "" && password != "" && confirmpassword != "" && password == confirmpassword && fullName != ""){
                if (bind.rbtnNhanvien.isChecked){
                    val user = User(
                        username,
                        password,
                        fullName
                    )
                    addMember(user)
                }
                //Thêm quản lý
                if (bind.rbtnQuanly.isChecked){
                    val admin = User(
                        username,
                        password,
                        fullName
                    )
                    addAdmin(admin)
                }
            }else{
                if (fullName == ""){
                    bind.edtFullName.error = "vui lòng họ tên"
                }
                if (username == ""){
                    bind.edtUsernameRegister.error = "Vui lòng nhập tên đăng nhập."
                }
                if (password == ""){
                    bind.edtPasswordRegister.error = "Vui lòng nhập mật khẩu."
                }
                if (confirmpassword == ""){
                    bind.edtConfirmPasswordRegister.error = "Vui lòng nhập xác nhận mật khẩu."
                }
                if (password != confirmpassword){
                    bind.edtConfirmPasswordRegister.error = "Xác nhận mật khẩu không đúng."
                }

            }


        }
        bind.edtPasswordRegister.setOnFocusChangeListener { view, b ->
            if (b){
                bind.tvCanhbao1.visibility = View.VISIBLE
                bind.tvCanhbao2.visibility = View.VISIBLE
            }else{
                bind.tvCanhbao1.visibility = View.GONE
                bind.tvCanhbao2.visibility = View.GONE
            }
        }
        bind.edtPasswordRegister.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            @SuppressLint("ResourceAsColor")
            override fun afterTextChanged(p0: Editable?) {
                var edtpassword = bind.edtPasswordRegister.text.toString()
                if (edtpassword.length >= 6){
                    bind.tvCanhbao1.setTextColor(ContextCompat.getColor(this@Register,
                        R.color.green
                    ))
                }else{
                    bind.tvCanhbao1.setTextColor(ContextCompat.getColor(this@Register,
                        R.color.textgrey
                    ))
                }
                var text = false
                var number = false
                for (str in edtpassword.toCharArray()){
                    if (str.isLetter())
                        text = true
                    if (str.isDigit())
                        number = true
                }
                if (text && number){
                    bind.tvCanhbao2.setTextColor(ContextCompat.getColor(this@Register,
                        R.color.green
                    ))
                }else{
                    bind.tvCanhbao2.setTextColor(ContextCompat.getColor(this@Register,
                        R.color.textgrey
                    ))
                }

            }

        })
    }
    fun addMember(user: User){
        val memberDatabase = FirebaseDatabase.getInstance().getReference("NhanVien")
        val userID = memberDatabase.push().key!!
        memberDatabase.child(userID).setValue(user).addOnSuccessListener {
            Toast.makeText(this@Register, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this@Register, "Đăng ký thất bại", Toast.LENGTH_SHORT)
        }
    }
    fun addAdmin(admin: User){

        val memberDatabase = FirebaseDatabase.getInstance().getReference("QuanLy")
        val userID = memberDatabase.push().key!!
        memberDatabase.setValue(userID).addOnSuccessListener {
            Toast.makeText(this@Register, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this@Register, "Đăng ký thất bại", Toast.LENGTH_SHORT)
        }
    }
    fun addNhanVien(username: String, password : String, userRef : DatabaseReference) {
        userRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(datasnapshot: DataSnapshot) {
                var check : Boolean = true
                for (snapshot in datasnapshot.children){
                    var user = snapshot.getValue(User::class.java)
                    if (user != null && user.username == username){
                        Toast.makeText(this@Register, "Tên đăng nhập đã tồn tại.", Toast.LENGTH_SHORT).show()
                        check = false
                        return
                    }
                }
                if (check){
                    // Tạo một nút con mới bằng cách sử dụng push() để tạo id ngẫu nhiên cho người dùng
                    val newUserRef = userRef.push().key!!

                    //Tạo đối tượng
                    var user1 = User(username, password)

                    //truy cập đến id vừa tạo và đặt giá trị = đối tượng user1
                    userRef.child(newUserRef).setValue(user1).addOnCompleteListener {
                        Toast.makeText(this@Register, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this@Register, "Đăng ký thất bại", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    }
