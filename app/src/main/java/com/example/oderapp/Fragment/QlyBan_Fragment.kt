package com.example.oderapp.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oderapp.Adapter.QlBan_Adapter
import com.example.oderapp.Interface.itf_UD_QLBan
import com.example.oderapp.Model.Ban
import com.example.oderapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [QlyBan_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QlyBan_Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var listBan : MutableList<Ban>
    lateinit var adapterBan : QlBan_Adapter
    lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_tatca__ql_ban, container, false)
        var rcTatca_qlBan = view.findViewById<RecyclerView>(R.id.rc_tatca_qlBan)
        var tenKhuVuc = arguments?.getString("a")
        listBan = mutableListOf()
        if(tenKhuVuc == null){
            listBan.clear()
            getDataBan()
        }else{
            listBan.clear()
            getBan(tenKhuVuc)
        }

        val listener = object : itf_UD_QLBan {
            override fun onClickUpdate(itemData: Ban, pos: Int) {
                dialogUpdate(itemData.id.toString(),itemData.tenKhuVuc.toString(), itemData.tenBan.toString(), pos)
            }

            override fun onClickDelete(itemData: Ban, pos: Int) {
                delete(itemData.id.toString(), itemData.tenBan.toString())
            }
        }
        listBan = mutableListOf()
        adapterBan = QlBan_Adapter(listBan, listener)
        rcTatca_qlBan.layoutManager = LinearLayoutManager(context,  LinearLayoutManager.VERTICAL, false)
        rcTatca_qlBan.adapter = adapterBan

        return view
    }
    fun dialogUpdate(id: String,tenKhuVuc : String, tenBan: String, pos : Int) {
        val build = AlertDialog.Builder(requireContext())
        build.setTitle("Sửa thông tin bàn.")
        val inputText = EditText(requireContext())
        inputText.hint = "Nhập tên bàn"
        inputText.setText(tenBan)
        build.setView(inputText)

        build.setPositiveButton("OK") { dialog, which ->
            val newName = inputText.text.toString()
            if (inputText.text.toString() == "") {
                inputText.error = "Vui lòng nhập tên bàn"
            } else {
                suaBan(id,tenKhuVuc, newName, pos)
                dialog.dismiss()
            }
        }
        build.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        build.show()
    }

    fun suaBan(id: String,tenKhuVuc: String, newName: String, pos : Int) {
        dbRef = FirebaseDatabase.getInstance().getReference("Ban")
        dbRef.orderByChild("tenBan").equalTo(newName).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var check = true
                for (it in snapshot.children) {
                    var ban = it.getValue(Ban::class.java)
                    if (ban != null && ban.tenBan == newName) {
                        Toast.makeText(
                            requireContext(),
                            "Bàn đã tồn tại",
                            Toast.LENGTH_SHORT
                        ).show()
                        check = false
                        return
                    }
                }
                if(check){
                    dbRef = FirebaseDatabase.getInstance().getReference("Ban").child(id)
                    var ban = Ban(id,tenKhuVuc, newName)
                    dbRef.setValue(ban).addOnCompleteListener {
                        listBan[pos].tenBan = newName
                        adapterBan.notifyDataSetChanged()
                        Toast.makeText(requireContext(), "Sửa thành công", Toast.LENGTH_SHORT)
                            .show()
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), "Lỗi", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    fun delete(id : String, tenBan : String){
        val build = AlertDialog.Builder(requireContext())
        build.setTitle("Xác nhận xóa $tenBan?")
        build.setPositiveButton("OK"){ dialog, which ->
            xoaBan(id, tenBan)
            dialog.dismiss()
        }
        build.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        build.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun xoaBan(id: String, tenBan: String) {
        dbRef = FirebaseDatabase.getInstance().getReference("Ban").child(id)
        val kq = dbRef.removeValue()
        kq.addOnSuccessListener {
            listBan.removeIf { it.id == id }
            adapterBan.notifyDataSetChanged()
            Toast.makeText(requireContext(), "Đã xóa $tenBan", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Lỗi", Toast.LENGTH_SHORT).show()
        }
    }
    private fun getBan(tenKhuVuc: String) {
        var dbRef : DatabaseReference = FirebaseDatabase.getInstance().getReference("Ban")
        dbRef.orderByChild("tenKhuVuc").equalTo(tenKhuVuc).addListenerForSingleValueEvent(object :
            ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                for (it in snapshot.children){
                    var ban = it.getValue(Ban::class.java)
                    listBan.add(ban!!)
                    adapterBan.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    fun getDataBan(){
        var dbRef : DatabaseReference = FirebaseDatabase.getInstance().getReference("Ban")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (it in snapshot.children) {
                        val kv = it.getValue(Ban::class.java)
                        listBan.add(kv!!)
                        adapterBan.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QlyBan_Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}