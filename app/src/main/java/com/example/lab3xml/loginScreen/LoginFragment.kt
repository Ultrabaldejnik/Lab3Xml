package com.example.lab3xml.loginScreen

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.lab3xml.MainActivity
import com.example.lab3xml.databinding.FragmentLoginBinding
import com.example.lab3xml.sharedPref.DataStore


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!

    private val vm: LoginViewModel by viewModels { LoginViewModel.factory }



    private var login =""
    private var password = ""
    private var contextF : Context? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        contextF = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVM()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        initUI()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun initVM() {
        vm.toastMessage.observe(this) { msg ->
            if (msg == "") return@observe
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
        }
        vm.nextScreen.observe(this) {
            (requireActivity() as MainActivity).launchListNoteFragment()
        }
    }

    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
    private fun initUI() {
        login = vm.login.value?.text.toString()
        password = vm.password.value?.text.toString()
        Log.d("ELEOT","login is $login")
        with(binding) {
            etLogin.text = login.toEditable()
            etLogin.addTextChangedListener(
                onTextChanged = { text, _, _, _ ->
                vm.updateLogin(text.toString())
                vm.saveUser()
            })

            etPassword.setText(password)
            etPassword.addTextChangedListener(onTextChanged = { text, _, _, _ ->
                vm.updatePassword(text.toString())
                vm.saveUser()
            })

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