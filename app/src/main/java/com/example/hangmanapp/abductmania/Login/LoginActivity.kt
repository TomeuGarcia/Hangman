package com.example.hangmanapp.abductmania.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

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
            val email = binding.emailInputEditText.text.toString()
            val password = binding.passwordInputEditText.text.toString()

            binding.loginButton.setTextColor(getColor(R.color.purple_dark)) // Show darker color when held

            loginViewModel.signIn(this, email, password,
                                   this::startMainMenuActivity, this::displayErrorUsernameOrPassword)

            binding.loginButton.setTextColor(getColor(R.color.green_soft)) // Return color to normal
        }

        binding.registerButton.setOnClickListener {
            startRegisterActivity()
        }

        binding.guestButton.setOnClickListener {
            loginViewModel.signInAsGuest(this::startMainMenuActivity, this::displayErrorUsernameOrPassword)
        }
    }


    private fun startMainMenuActivity()
    {
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startRegisterActivity()
    {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun displayErrorUsernameOrPassword()
    {
        Toast.makeText(this, getString(R.string.errorUsernameOrPassword),
            Toast.LENGTH_LONG).show()
    }

}