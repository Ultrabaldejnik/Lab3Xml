package com.example.lab3xml.listScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.lab3xml.App
import com.example.lab3xml.loginScreen.LoginViewModel
import com.example.lab3xml.sharedPref.DataStore
import com.example.labwork3.dbNotes.Note
import com.example.labwork3.dbNotes.NoteDao
import com.example.labwork3.dbUser.UserDao
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

class ListNoteViewModel(private val db: NoteDao, private val localStore: DataStore) : ViewModel() {

    private var _listNote: LiveData<List<Note>>
    val listNote: LiveData<List<Note>>
        get() = _listNote

    private var _toastMessage = MutableLiveData<Unit>()
    val toastMessage: LiveData<Unit>
        get() = _toastMessage


    val userName = localStore.getUser().login

    init {
        _listNote = db.getNotes(userName)
    }

     fun addUI(text: String) {
        if (text.isBlank()) {
            _toastMessage.value = Unit
            return
        }
        addNote(text)

    }

    private fun addNote(text: String) = viewModelScope.launch {
        val date = Calendar.getInstance().time
        val sdf = SimpleDateFormat("dd/MM/yy HH:mm")
        val formattedDate = sdf.format(date).toString()
        val note = Note(owner = userName, value = text, creationDateTime = formattedDate)
        db.upsertNote(note)
    }


    companion object {
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val db =
                    (checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as App).dbNotes.dao
                val store =
                    (checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as App).dbDataStore
                return ListNoteViewModel(db, store) as T
            }
        }
    }
}