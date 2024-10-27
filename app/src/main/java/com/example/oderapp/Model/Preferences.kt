package com.example.quanlydiem

import android.content.Context


class Preferences (context : Context) {
    val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val editor = sharedPref.edit()

    fun savelogin(fullName : String, isAdmin: Boolean){
        if (isAdmin==true){
            editor.putString("fullName", fullName)
            editor.putBoolean("isAdmin", true)
            editor.putBoolean("islogin", true)
            editor.apply()
        }else{
            editor.putString("fullName", fullName)
            editor.putBoolean("isAdmin", false)
            editor.putBoolean("islogin", true)
            editor.apply()
        }

    }
    fun isAdmin(): Boolean{
        return sharedPref.getBoolean("isAdmin", false)
    }
    fun islogin(): Boolean{
        return sharedPref.getBoolean("islogin", false)
    }
    fun getFullName(): String? {
        return sharedPref.getString("fullName", "")
    }

    fun logout(){
        editor.remove("id")
        editor.remove("isAdmin")
        editor.remove("islogin")
        editor.remove("remember")
        editor.apply()
    }

}