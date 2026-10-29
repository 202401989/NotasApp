package com.efer.notasapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.efer.notasapp.data.NotesRepository
import com.efer.notasapp.data.SessionManager
import com.efer.notasapp.viewmodel.NotesViewModel
import com.efer.notasapp.viewmodel.NotesViewModelFactory

class CreateNoteActivity : AppCompatActivity() {

    private lateinit var viewModel: NotesViewModel

    private lateinit var edtNoteTitle: EditText
    private lateinit var edtNoteContent: EditText
    private lateinit var btnSaveNote: Button
    private lateinit var progressCreateNote: ProgressBar
    private lateinit var txtCreateMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

        edtNoteTitle = findViewById(R.id.edtNoteTitle)
        edtNoteContent = findViewById(R.id.edtNoteContent)
        btnSaveNote = findViewById(R.id.btnSaveNote)
        progressCreateNote = findViewById(R.id.progressCreateNote)
        txtCreateMessage = findViewById(R.id.txtCreateMessage)

        val sessionManager = SessionManager(applicationContext)
        val repository = NotesRepository(sessionManager)
        val factory = NotesViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)[NotesViewModel::class.java]

        observeViewModel()

        btnSaveNote.setOnClickListener {
            val title = edtNoteTitle.text.toString().trim()
            val content = edtNoteContent.text.toString().trim()

            viewModel.createNote(title, content)
        }
    }

    private fun observeViewModel() {
        viewModel.loading.observe(this) { isLoading ->
            progressCreateNote.visibility =
                if (isLoading) View.VISIBLE else View.GONE

            btnSaveNote.isEnabled = !isLoading
        }

        viewModel.message.observe(this) { message ->
            txtCreateMessage.text = message.orEmpty()

            val isSuccess = message == "Nota guardada correctamente."

            txtCreateMessage.setTextColor(
                if (isSuccess) {
                    android.graphics.Color.rgb(22, 101, 52)
                } else {
                    android.graphics.Color.rgb(185, 28, 28)
                }
            )
        }

        viewModel.noteCreated.observe(this) { success ->
            if (success) {
                edtNoteTitle.text.clear()
                edtNoteContent.text.clear()
                viewModel.resetNoteCreated()
            }
        }
    }
}

