package com.efer.notasapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.efer.notasapp.data.SessionManager

class DashboardActivity : AppCompatActivity() {


    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        sessionManager = SessionManager(applicationContext)

        val txtWelcome = findViewById<TextView>(R.id.txtWelcome)
        val btnCreateNote = findViewById<Button>(R.id.btnCreateNote)
        val btnListNotes = findViewById<Button>(R.id.btnListNotes)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        val email = sessionManager.getUserEmail()
        txtWelcome.text = if (!email.isNullOrBlank()) {
            "¡Bienvenido!\n$email"
        } else {
            "¡Bienvenido a tus notas!"
        }

        btnCreateNote.setOnClickListener {
            startActivity(
                Intent(this, CreateNoteActivity::class.java)
            )
        }

        btnListNotes.setOnClickListener {
            startActivity(
                Intent(this, NotesListActivity::class.java)
            )
        }

        btnLogout.setOnClickListener {
            sessionManager.clearSession()

            val intent = Intent(this, MainActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
            finish()
        }
    }


}
