package com.example.oderapp.Interface

import com.example.oderapp.Model.Ban

interface itf_UD_QLBan {
    fun onClickUpdate(itemData : Ban, pos : Int)
    fun onClickDelete(itemData : Ban, pos : Int)
}