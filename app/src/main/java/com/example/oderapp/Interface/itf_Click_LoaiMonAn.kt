package com.example.oderapp.Interface

import com.example.oderapp.Model.KhuVucBan
import com.example.oderapp.Model.LoaiMonAn

interface itf_Click_LoaiMonAn {
    fun onClick(itemData : LoaiMonAn, pos : Int, posSelected : Int)
}