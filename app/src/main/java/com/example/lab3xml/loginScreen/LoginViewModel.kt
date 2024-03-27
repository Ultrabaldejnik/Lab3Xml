package com.example.lab3xml.loginScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.lab3xml.App
import com.example.lab3xml.LogAction
import com.example.lab3xml.sharedPref.DataStore
import com.example.labwork3.dbUser.User
import com.example.labwork3.dbUser.UserDB
import kotlinx.coroutines.launch

class LoginViewModel(dbUser: UserDB, private val localStore: DataStore) : ViewModel() {


    private var _toastMessage = MutableLiveData<String>("")
    val toastMessage: LiveData<String>
        get() = _toastMessage
    private var _nextScreen = MutableLiveData<Boolean>(false)
    val nextScreen : LiveData<Boolean>
        get() = _nextScreen


    private var _login: StateUI = StateUI("")
    val login get() = _login.text
    private var _password = StateUI("")
    val password get() = _password.text
    private val dbUser = dbUser.dao

    init {
        initUI()
    }


    private fun initUI() {
        val user = localStore.getUser()
        _login = _login.copy(text = user.login)
        _password = _password.copy(text = user.password)
    }

    private fun validateLogin() {
        _login = if (_login.text.isBlank()) {
            _login.copy(errorMessage = true)
        } else {
            _login.copy(errorMessage = false)
        }
    }

    private fun validatePassword() {
        _password = if (_password.text.isBlank()) {
            _password.copy(errorMessage = true)
        } else {
            _password.copy(errorMessage = false)
        }
    }

    private fun addUser(login: String, password: String) = viewModelScope.launch {
        val user = User(login = login, password = password)
        dbUser.addUser(user)
    }

    fun validate() {
        validateLogin()
        validatePassword()
        if (!_login.errorMessage && !_password.errorMessage) {
            signIn(_login.text, _password.text)
        } else {
            sendMessage(LogAction.DataWrong)
        }
    }

    private fun signIn(login: String, password: String) {
        viewModelScope.launch {

            if (checkUserExist(login) == 0) {
                addUser(login, password)
                sendMessage(LogAction.NewUser)
                _nextScreen.value = true
                return@launch
            }
            if (checkUserExist(login) == 1) {
                val user = dbUser.getUser(login)
                if (password == user.password) {
                    sendMessage(LogAction.Welcome)
                    _nextScreen.value = true
                    return@launch
                }else{
                    sendMessage(LogAction.WrongPassword)
                    return@launch
                }
            }

        }
    }

    fun updateLogin(text: String) {
        _login = _login.copy(text = text);
    }

    fun updatePassword(text: String) {
        _password = _password.copy(text = text);
    }

    private suspend fun checkUserExist(login: String) = dbUser.checkedUser(login)


    private fun sendMessage(action: LogAction) {
        viewModelScope.launch {
            when (action) {
                LogAction.DataWrong -> _toastMessage.value = "Данные введены некорректно"
                LogAction.WrongPassword -> _toastMessage.value = "Неправильный пароль"
                LogAction.Welcome -> _toastMessage.value = "Здраствуйте, $_login"
                LogAction.NewUser -> _toastMessage.value = "Добро пожаловать, $_login"
            }
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val db =
                    (checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as App).dbUser
                val store =
                    (checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as App).dbDataStore
                return LoginViewModel(db, store) as T
            }
        }
    }
}

data class StateUI(val text: String, var errorMessage: Boolean = false)