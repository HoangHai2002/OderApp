package com.example.oderapp.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import kotlin.properties.Delegates

class Register : AppCompatActivity() {
    lateinit var userRef : DatabaseReference
    lateinit var firebase : FirebaseDatabase
    lateinit var bind : ActivityRegisterBinding
    var checkPassWordLength : Boolean = false
    var checkPassWordStyle : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bind = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(bind.root)




        val nhanVienRef = FirebaseDatabase.getInstance().getReference("NhanVien")
        val quanLyRef = FirebaseDatabase.getInstance().getReference("QuanLy")

        bind.tvRedirectLoginRegister.setOnClickListener {
            finish()
        }
        bind.btnRegisterRegister.setOnClickListener {
            val username = bind.edtUsernameRegister.text.toString()
            val password = bind.edtPasswordRegister.text.toString()
            val fullName = bind.edtFullName.text.toString()
            val confirmpassword = bind.edtConfirmPasswordRegister.text.toString()
            if(username != "" && password != "" && confirmpassword != "" &&
                password == confirmpassword && fullName != "" &&
                checkPassWordLength && checkPassWordStyle){
                if (bind.rbtnNhanvien.isChecked){
                    val user = User(
                        username,
                        password,
                        fullName
                    )
                    themNguoiDung(user, nhanVienRef)
                }
                //Thêm quản lý
                if (bind.rbtnQuanly.isChecked){
                    val admin = User(
                        username,
                        password,
                        fullName
                    )
                    themNguoiDung(admin, quanLyRef)
                }
            }else{
                if (fullName == ""){
                    bind.edtFullName.error = "Vui lòng nhập họ tên"
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
                if (!checkPassWordStyle && !checkPassWordLength){
                    bind.edtPasswordRegister.error = "Mật khẩu không hợp lệ."
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
                    checkPassWordLength = true
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
                    checkPassWordStyle = true
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
        memberDatabase.child(userID).setValue(admin).addOnSuccessListener {
            Toast.makeText(this@Register, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this@Register, "Đăng ký thất bại", Toast.LENGTH_SHORT)
        }
    }
    fun themNguoiDung(user : User, userRef : DatabaseReference) {
        userRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(datasnapshot: DataSnapshot) {
                var check : Boolean = true
                for (snapshot in datasnapshot.children){
                    var newUser = snapshot.getValue(User::class.java)
                    Log.d("11111111111111111111", "${newUser?.fullName} , ${user?.fullName}")
                    if (newUser != null && newUser.fullName == user.fullName){
                        Toast.makeText(this@Register, "Tên người dùng đã tồn tại.", Toast.LENGTH_SHORT).show()
                        check = false
                        return
                    }
                    if (newUser != null && newUser.username == user.username){
                        Toast.makeText(this@Register, "Tên đăng nhập đã tồn tại.", Toast.LENGTH_SHORT).show()
                        check = false
                        return
                    }
                }
                if (check){
                    val newUserRef = userRef.push().key!!
                    userRef.child(newUserRef).setValue(user).addOnCompleteListener {
                        Toast.makeText(this@Register, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this@Register, "Đăng ký thất bại", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    }
