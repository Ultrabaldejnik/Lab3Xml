package com.example.lab3xml

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.lab3xml.listScreen.ListNoteFragment
import com.example.lab3xml.loginScreen.LoginFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        launchListUserFragment()
    }

    private fun launchListUserFragment() {
        if (supportFragmentManager.findFragmentByTag(LoginFragment.TAG) == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)

                add<LoginFragment>(R.id.container, LoginFragment.TAG)
            }
        }
    }

    fun exit(){
        supportFragmentManager.popBackStack()
    }

    fun launchListNoteFragment(){
        if(supportFragmentManager.findFragmentByTag(ListNoteFragment.TAG) == null){
            supportFragmentManager.commit {
                setReorderingAllowed(true)

                add<ListNoteFragment>(R.id.container, ListNoteFragment.TAG)
                addToBackStack(ListNoteFragment.TAG)
            }
        }
    }
}