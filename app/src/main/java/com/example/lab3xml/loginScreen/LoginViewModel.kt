package com.example.lab3xml.loginScreen

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.lab3xml.App
import com.example.lab3xml.LogAction
import com.example.lab3xml.sharedPref.DataStore
import com.example.lab3xml.sharedPref.Settings
import com.example.labwork3.dbUser.User
import com.example.labwork3.dbUser.UserDB
import com.example.labwork3.dbUser.UserDao
import kotlinx.coroutines.launch

class LoginViewModel(private val dbUser: UserDao, private val localStore: DataStore) : ViewModel() {




    private var _toastMessage = MutableLiveData("")
    val toastMessage: LiveData<String>
        get() = _toastMessage
    private var _nextScreen = MutableLiveData<Unit>()
    val nextScreen : LiveData<Unit>
        get() = _nextScreen


    private var _login = MutableLiveData<StateUI>()
    val login : LiveData<StateUI>
        get() = _login

    private var _password = MutableLiveData<StateUI>()
    val password : LiveData<StateUI>
        get() = _password



    init {
        initUI()
    }

    fun saveUser(){
        val user = Settings(_login.value?.text!!,password.value?.text!!)
        localStore.saveUser(user)
    }
    private fun initUI() {
        val user = localStore.getUser()
        val login = StateUI(user.login)
        val password = StateUI(user.password)
        _login.value = login
        _password.value= password
    }

    private fun validateLogin() {
        _login.value = if (_login.value?.text?.isBlank() == true) {
            _login.value?.copy(errorMessage = true)
        } else {
            _login.value?.copy(errorMessage = false)
        }

    }

    private fun validatePassword() {
        _password.value = if (_password.value?.text?.isBlank()== true) {
            _password.value?.copy(errorMessage = true)
        } else {
            _password.value?.copy(errorMessage = false)
        }

    }

    private fun addUser(login: String, password: String) = viewModelScope.launch {
        val user = User(login = login, password = password)
        dbUser.addUser(user)
    }

    fun validate() {
        validateLogin()
        validatePassword()
        if (!_login.value?.errorMessage!! && !_password.value?.errorMessage!!) {
            signIn(_login.value?.text!!, _password.value?.text!!)
        } else {
            sendMessage(LogAction.DataWrong)
        }
    }

    private fun signIn(login: String, password: String) {
        viewModelScope.launch {
            if (checkUserExist(login) == 0) {
                addUser(login, password)
                sendMessage(LogAction.NewUser)
                _nextScreen.value = Unit
                return@launch
            }
            if (checkUserExist(login) == 1) {
                val user = dbUser.getUser(login)
                if (password == user.password) {
                    sendMessage(LogAction.Welcome)
                    _nextScreen.value = Unit
                    return@launch
                }else{
                    sendMessage(LogAction.WrongPassword)
                    return@launch
                }
            }
        }
    }

    fun updateLogin(text: String) {
        _login.value = _login.value?.copy(text = text)
    }

    fun updatePassword(text: String) {
        _password.value = _password.value?.copy(text = text)
    }

    private suspend fun checkUserExist(login: String) = dbUser.checkedUser(login)


    private fun sendMessage(action: LogAction) {
        viewModelScope.launch {
            when (action) {
                LogAction.DataWrong -> _toastMessage.value = "Данные введены некорректно"
                LogAction.WrongPassword -> _toastMessage.value = "Неправильный пароль"
                LogAction.Welcome -> _toastMessage.value = "Здраствуйте, ${_login.value?.text}"
                LogAction.NewUser -> _toastMessage.value = "Добро пожаловать, ${_login.value?.text}"
            }
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val db =
                    (checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as App).dbUser.dao
                val store =
                    (checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as App).dbDataStore
                return LoginViewModel(db, store) as T
            }
        }
    }
}

data class StateUI(val text: String, var errorMessage: Boolean = false)