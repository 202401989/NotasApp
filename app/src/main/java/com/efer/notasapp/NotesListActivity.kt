package com.efer.notasapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.efer.notasapp.data.NotesRepository
import com.efer.notasapp.data.SessionManager
import com.efer.notasapp.viewmodel.NotesViewModel
import com.efer.notasapp.viewmodel.NotesViewModelFactory

class NotesListActivity : AppCompatActivity() {

    private lateinit var viewModel: NotesViewModel
    private lateinit var notesAdapter: NotesAdapter

    private lateinit var progressNotes: ProgressBar
    private lateinit var txtNotesMessage: TextView
    private lateinit var recyclerNotes: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_list)

        progressNotes = findViewById(R.id.progressNotes)
        txtNotesMessage = findViewById(R.id.txtNotesMessage)
        recyclerNotes = findViewById(R.id.recyclerNotes)

        val btnBackDashboard = findViewById<Button>(R.id.btnBackDashboard)

        recyclerNotes.layoutManager = LinearLayoutManager(this)
        notesAdapter = NotesAdapter()
        recyclerNotes.adapter = notesAdapter

        val sessionManager = SessionManager(applicationContext)
        val repository = NotesRepository(sessionManager)
        val factory = NotesViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)[NotesViewModel::class.java]

        observeViewModel()

        btnBackDashboard.setOnClickListener {
            finish()
        }

        viewModel.loadNotes()
    }

    private fun observeViewModel() {
        viewModel.loading.observe(this) { isLoading ->
            progressNotes.visibility =
                if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.message.observe(this) { message ->
            txtNotesMessage.text = message.orEmpty()
        }

        viewModel.notes.observe(this) { notes ->
            notesAdapter.updateNotes(notes)

            if (notes.isEmpty()) {
                txtNotesMessage.text = "Todavía no tienes notas guardadas."
            } else {
                txtNotesMessage.text = ""
            }
        }
    }
}

