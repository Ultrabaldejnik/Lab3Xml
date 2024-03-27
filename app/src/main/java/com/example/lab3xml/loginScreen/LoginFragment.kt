package com.example.lab3xml.loginScreen

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.lab3xml.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!

    private val vm: LoginViewModel by viewModels { LoginViewModel.factory }

    private var login = ""
    private var password = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        login = vm.login.toString()
        password = vm.password.toString()
        initVM()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun initVM() {
        vm.toastMessage.observe(this) { msg ->
            if (msg == "") return@observe
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
        }
        vm.nextScreen.observe(this) {
            Log.d("ELEOT", "$it")
        }
    }


    private fun init() {
        with(binding) {
            etLogin.setText(login)
            etPassword.setText(password)
            btnSign.setOnClickListener {
                vm.updateLogin(etLogin.text.toString())
                vm.updatePassword(etPassword.text.toString())
                vm.validate()
            }
        }
    }

    companion object {
        const val TAG = "login_fragment"
    }
}