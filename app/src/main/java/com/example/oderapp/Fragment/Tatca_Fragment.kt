package com.example.oderapp.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oderapp.Activity.BanSelectedActivity
import com.example.oderapp.Activity.OrderActivity
import com.example.oderapp.Adapter.Ban_Adapter
import com.example.oderapp.Interface.itf_Click_Ban
import com.example.oderapp.Model.Ban
import com.example.oderapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Tatca_Fragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    lateinit var listBan : MutableList<Ban>
    lateinit var adapterBan : Ban_Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_tacca_, container, false)
        var rcTatca = view.findViewById<RecyclerView>(R.id.rc_tatca)
        var tenKhuVuc = arguments?.getString("a")
        listBan = mutableListOf()
        if(tenKhuVuc == null){
            listBan.clear()
            getDataBan()
        }else{
            listBan.clear()
            getBan(tenKhuVuc)
        }

        var listener = object : itf_Click_Ban{
            override fun onClick(itemData: Ban, pos: Int) {
                if(itemData.trangThai == false){
                    var intent = Intent(context, OrderActivity::class.java)
                    intent.putExtra("id", itemData.id)
                    startActivity(intent)
                }else{
                    var intent = Intent(context, BanSelectedActivity::class.java)
                    intent.putExtra("id", itemData.id)
                    startActivity(intent)
                }
            }
        }

        adapterBan  = Ban_Adapter(listBan, listener)
        rcTatca.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        rcTatca.adapter = adapterBan

        return view
    }

    private fun getBan(tenKhuVuc: String) {
        var dbRef : DatabaseReference = FirebaseDatabase.getInstance().getReference("Ban")
        dbRef.orderByChild("tenKhuVuc").equalTo(tenKhuVuc).addListenerForSingleValueEvent(object : ValueEventListener{
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
            Tatca_Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}