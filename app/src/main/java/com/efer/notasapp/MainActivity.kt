package com.efer.notasapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.efer.notasapp.data.NotesRepository
import com.efer.notasapp.data.SessionManager
import com.efer.notasapp.viewmodel.NotesViewModel
import com.efer.notasapp.viewmodel.NotesViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {


    private lateinit var viewModel: NotesViewModel

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressLogin: ProgressBar
    private lateinit var txtMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.btnLogin)
        progressLogin = findViewById(R.id.progressLogin)
        txtMessage = findViewById(R.id.txtMessage)

        val sessionManager = SessionManager(applicationContext)
        val repository = NotesRepository(sessionManager)
        val factory = NotesViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)[NotesViewModel::class.java]

        observeViewModel()

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString()

            viewModel.login(email, password)
        }
    }

    private fun observeViewModel() {
        viewModel.loading.observe(this) { isLoading ->
            progressLogin.visibility =
                if (isLoading) View.VISIBLE else View.GONE

            btnLogin.isEnabled = !isLoading
        }

        viewModel.message.observe(this) { message ->
            txtMessage.text = message.orEmpty()
        }

        viewModel.loginSuccess.observe(this) { success ->
            if (success) {
                openDashboard()
            }
        }
    }


    private fun openDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }


}
