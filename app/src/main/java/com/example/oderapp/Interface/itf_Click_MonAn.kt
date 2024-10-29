package com.example.oderapp.Interface

import com.example.oderapp.Model.KhuVucBan
import com.example.oderapp.Model.LoaiMonAn
import com.example.oderapp.Model.MonAn

interface itf_Click_MonAn {
    fun onClickMonAn(itemData : MonAn, gia : Int, count : Int)
    fun onClickXoa(id : String?, pos : Int)
}