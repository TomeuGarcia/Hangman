package com.example.hangmanapp.abductmania.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.KeyListener
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.hangmanapp.R
import com.example.hangmanapp.abductmania.MainMenu.MainMenuActivity
import com.example.hangmanapp.abductmania.Register.RegisterActivity
import com.example.hangmanapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel : LoginViewModel by viewModels()

    private lateinit var emailKeyListener : KeyListener
    private lateinit var passwordKeyListener : KeyListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        emailKeyListener = binding.emailInputEditText.keyListener
        passwordKeyListener = binding.passwordInputEditText.keyListener

        if (loginViewModel.hasLoggedUserAlready())
        {
            startMainMenuActivity()
        }


        binding.loadingBar.visibility = View.INVISIBLE

        binding.loadingBar.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus)
            {
                val username = binding.emailInputEditText.text.toString()
                if (loginViewModel.isValidUsername(username))
                {
                    binding.emailInputEditText.error = null
                }
                else
                {
                    binding.emailInputEditText.error = "Invalid username"
                }
            }
        }

        binding.loginButton.setOnClickListener {
            tryLogin()
        }

        binding.registerButton.setOnClickListener {
            disableButtons()
            startRegisterActivity()
        }

        binding.guestText.setOnClickListener {
            disableButtons()
            loginAsGuest()
        }
    }


    private fun startMainMenuActivity()
    {
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
        finish()

        binding.loadingBar.visibility = View.VISIBLE
    }

    private fun startRegisterActivity()
    {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()

        binding.loadingBar.visibility = View.VISIBLE
    }

    private fun displayErrorUsernameOrPassword()
    {
        enableButtons()

        Toast.makeText(this, getString(R.string.errorUsernameOrPassword),
            Toast.LENGTH_LONG).show()
    }

    private fun disableButtons()
    {
        binding.loginButton.isEnabled = false
        binding.registerButton.isEnabled = false
        binding.guestText.isEnabled = false

        binding.emailInputEditText.keyListener = null
        binding.passwordInputEditText.keyListener = null
    }

    private fun enableButtons()
    {
        binding.loginButton.isEnabled = true
        binding.registerButton.isEnabled = true
        binding.guestText.isEnabled = true

        binding.emailInputEditText.keyListener = emailKeyListener
        binding.passwordInputEditText.keyListener = passwordKeyListener
    }

    private fun tryLogin()
    {
        val email = binding.emailInputEditText.text.toString()
        val password = binding.passwordInputEditText.text.toString()
        if (email.isEmpty() || password.isEmpty())
        {
            displayErrorUsernameOrPassword()
            return
        }

        disableButtons()

        loginViewModel.signIn(this, email, password,
            this::startMainMenuActivity, this::displayErrorUsernameOrPassword)
    }

    private fun loginAsGuest()
    {
        binding.guestText.setTextColor(getColor(R.color.green_strong_selected))
        binding.loadingBar.visibility = View.VISIBLE

        loginViewModel.signInAsGuest(this::startMainMenuActivity, this::displayErrorUsernameOrPassword)
    }

}