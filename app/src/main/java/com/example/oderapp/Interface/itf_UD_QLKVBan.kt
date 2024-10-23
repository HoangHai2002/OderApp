package com.example.oderapp.Interface

import com.example.oderapp.Model.KhuVucBan

interface itf_UD_QLKVBan {
    fun onClickUpdate(itemData : KhuVucBan, pos : Int)
    fun onClickDelete(itemData : KhuVucBan, pos : Int)
}