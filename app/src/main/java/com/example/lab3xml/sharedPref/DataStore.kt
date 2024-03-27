package com.example.lab3xml.sharedPref

import android.content.Context
import android.content.Context.MODE_PRIVATE


class DataStore(context : Context) {
    private val sharedPref = context.getSharedPreferences("MySharedPref",MODE_PRIVATE);

    fun saveUser(user : Settings){
        with (sharedPref.edit()) {
            putString("login",user.login)
            putString("password",user.password)
            apply()
        }
    }

    fun getUser(): Settings {
        val login = sharedPref.getString("login","")!!
        val password = sharedPref.getString("login","")!!
        return Settings(login,password)
    }
}

data class Settings(
    val login: String,
    val password: String
)