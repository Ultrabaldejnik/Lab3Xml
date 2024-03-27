package com.example.lab3xml.sharedPref

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log


class DataStore(context : Context) {
    private val sharedPref = context.getSharedPreferences("MySharedPref",MODE_PRIVATE);

    fun saveUser(user : Settings){
        with (sharedPref.edit()) {
            putString("login",user.login)
            putString("password",user.password)
            Log.d("DATASTORE","saveuser ${user.login} ${user.password}")
            apply()
        }
    }

    fun getUser(): Settings {
        val login = sharedPref.getString("login","")!!
        val password = sharedPref.getString("password","")!!
        Log.d("DATASTORE","getuser $login $password")
        return Settings(login,password)
    }
}

data class Settings(
    val login: String,
    val password: String
)