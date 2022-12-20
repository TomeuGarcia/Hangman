package com.example.hangmanapp.abductmania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.hangmanapp.R
import com.example.hangmanapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel : RegisterViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.progressBar.visibility = View.INVISIBLE


        binding.emailInputEditText.setOnFocusChangeListener { view, focused ->
            if (!focused) {
                binding.emailInputLayout.helperText =
                    registerViewModel.validateEmail(binding.passwordInputEditText.text.toString())
            }
        }

        binding.userInputEditText.setOnFocusChangeListener { view, focused ->
            if (!focused) {
                binding.usernameInputLayout.helperText =
                    registerViewModel.validateUsername(binding.userInputEditText.text.toString())
            }
        }

        binding.passwordInputEditText.setOnFocusChangeListener { view, focused ->
            if (!focused) {
                binding.passwordInputLayout.helperText =
                    registerViewModel.validatePassword(binding.passwordInputEditText.text.toString())
            }
        }

        binding.doRegisterButton.setOnClickListener {
            if (registerViewModel.canRegisterUser())
            {
                binding.progressBar.visibility = View.VISIBLE

                val email = binding.emailInputEditText.text.toString()
                val username = binding.userInputEditText.text.toString()
                val password = binding.passwordInputEditText.text.toString()

                registerViewModel.registerNewUser(this,
                                                  email, username, password,
                                                  this::startMainMenuActivity,
                                                  this::displayRegisterError)
            }
            else
            {
                Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backLoginButton.setOnClickListener {
            startLoginActivity()
        }
    }

    private fun startLoginActivity()
    {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun startMainMenuActivity()
    {
        binding.progressBar.visibility = View.INVISIBLE

        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }

    private fun displayRegisterError()
    {
        binding.progressBar.visibility = View.INVISIBLE

        Toast.makeText(this, getString(R.string.somethingWentWrong),
            Toast.LENGTH_SHORT).show()
    }


}