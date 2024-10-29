package com.example.oderapp.Model

class ThanhToan (
    var id : String? = "",
    var tenBan : String? = "",
    var tenKhuVuc : String? = "",
    var nguoiThanhToan : String? = "",
    var tongTien : Int? = 0,
    var monAn : MutableList<MonAnThanhToan> = mutableListOf(),
    var thoiGian : String? = "",
    var ngayThang : String? = "",
)