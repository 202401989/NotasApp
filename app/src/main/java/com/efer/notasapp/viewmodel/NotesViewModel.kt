package com.efer.notasapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efer.notasapp.data.AuthUser
import com.efer.notasapp.data.Note
import com.efer.notasapp.data.NotesRepository
import kotlinx.coroutines.launch

class NotesViewModel(
    private val repository: NotesRepository
) : ViewModel() {


    private val _user = MutableLiveData<AuthUser?>()
    val user: LiveData<AuthUser?> = _user

    private val _notes = MutableLiveData<List<Note>>(emptyList())
    val notes: LiveData<List<Note>> = _notes

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val _loginSuccess = MutableLiveData(false)
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val _noteCreated = MutableLiveData(false)
    val noteCreated: LiveData<Boolean> = _noteCreated

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _message.value = "Ingresa tu correo y contraseña."
            return
        }

        viewModelScope.launch {
            _loading.value = true
            _message.value = null
            _loginSuccess.value = false

            val result = repository.login(email, password)

            result.onSuccess { authenticatedUser ->
                _user.value = authenticatedUser
                _loginSuccess.value = true
            }.onFailure { error ->
                _message.value = error.message ?: "No se pudo iniciar sesión."
            }

            _loading.value = false
        }
    }

    fun loadNotes() {
        viewModelScope.launch {
            _loading.value = true
            _message.value = null

            val result = repository.getNotes()

            result.onSuccess { loadedNotes ->
                _notes.value = loadedNotes
            }.onFailure { error ->
                _message.value = error.message ?: "No se pudieron cargar las notas."
            }

            _loading.value = false
        }
    }

    fun createNote(title: String, content: String) {
        if (title.isBlank() || content.isBlank()) {
            _message.value = "Completa el título y el contenido."
            return
        }

        viewModelScope.launch {
            _loading.value = true
            _message.value = null
            _noteCreated.value = false

            val result = repository.createNote(title, content)

            result.onSuccess {
                _noteCreated.value = true
                _message.value = "Nota guardada correctamente."
            }.onFailure { error ->
                _message.value = error.message ?: "No se pudo guardar la nota."
            }

            _loading.value = false
        }
    }

    fun clearMessage() {
        _message.value = null
    }

    fun resetNoteCreated() {
        _noteCreated.value = false
    }


}
