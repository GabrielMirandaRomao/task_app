package com.devmasterteam.tasks.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.databinding.ActivityLoginBinding
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Variáveis da classe
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding = ActivityLoginBinding.inflate(layoutInflater)

        // Layout
        setContentView(binding.root)

        viewModel.verifyLoggedUser()

        // Observadores
        observe()
        setListener()
    }

    private fun setListener(){
        binding.buttonLogin.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()
            viewModel.doLogin(email, password)
        }
//        binding.textRegister.setOnClickListener(this)
    }

    private fun observe() {
        viewModel.login.observe(this) {
            if (it.showStatus()){
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }

            Toast.makeText(this, it.showMessage(), Toast.LENGTH_LONG).show()
        }

        viewModel.loggedUser.observe(this) {
            if(it) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
        }
    }
}