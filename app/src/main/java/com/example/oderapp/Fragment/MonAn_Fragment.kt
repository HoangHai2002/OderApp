package com.example.oderapp.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oderapp.Adapter.MonAn_Adapter
import com.example.oderapp.Interface.itf_Click_MonAn
import com.example.oderapp.Model.MonAn
import com.example.oderapp.R
import com.example.oderapp.databinding.FragmentMonAnBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class MonAn_Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var listMonAn: MutableList<MonAn>
    lateinit var adapter: MonAn_Adapter
    lateinit var dbRef: DatabaseReference

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
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_mon_an_, container, false)
        var rc_monAn = view.findViewById<RecyclerView>(R.id.rc_monAn)



        var tenLoaiMonAn = arguments?.getString("name")
        listMonAn = mutableListOf()
        if (tenLoaiMonAn == null) {
            listMonAn.clear()
            getAllMonAn()
        } else {
            listMonAn.clear()
            getMonAn(tenLoaiMonAn)
        }

        var listener = object : itf_Click_MonAn {
            override fun onClickMonAn(itemData: MonAn, pos: Int) {

            }

            override fun onClickCong(itemData: MonAn, pos: Int) {

            }

            override fun onClickTru(itemData: MonAn, pos: Int) {

            }

        }
        adapter = MonAn_Adapter(listMonAn, listener)
        rc_monAn.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rc_monAn.adapter = adapter

        return view
    }

    private fun getMonAn(tenLoaiMonAn: String) {
        dbRef = FirebaseDatabase.getInstance().getReference("MonAn")
        dbRef.orderByChild("tenLoaiMonAn").equalTo(tenLoaiMonAn)
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (it in snapshot.children) {
                        var monAn = it.getValue(MonAn::class.java)
                        listMonAn.add(monAn!!)
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }

    fun getAllMonAn() {
        var dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("MonAn")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (it in snapshot.children) {
                        val monAn = it.getValue(MonAn::class.java)
                        listMonAn.add(monAn!!)
                        adapter.notifyDataSetChanged()
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
            MonAn_Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}