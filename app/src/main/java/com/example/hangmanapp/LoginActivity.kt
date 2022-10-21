package com.example.hangmanapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.hangmanapp.databinding.ActivityLoginBinding
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val CORRECT_USERNAME = "tomeu.garcia@enti.cat"
    private val CORRECT_PASSWORD = "1234"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.INVISIBLE

        binding.userInput.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus)
            {
                val username = binding.userInput.text.toString()
                if (!Patterns.EMAIL_ADDRESS.matcher(username).matches())
                {
                    binding.userInput.error = "Invalid username"
                }
                else{
                    binding.userInput.error = null
                }
            }
        }

        binding.loginButton.setOnClickListener {
            val username = binding.userInput.text.toString()
            val password = binding.passwordInput.text.toString()

            val correctUsername = username == CORRECT_USERNAME
            val correctPassword = password == CORRECT_PASSWORD

            if (correctUsername && correctPassword)
            {
                // Can login
                binding.progressBar.visibility = View.VISIBLE

                CoroutineScope(Dispatchers.Default).launch {
                    delay(3000)

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }
            else
            {
                Toast.makeText(this, getString(R.string.errorUsernameOrPassword), Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun startAnimation()
    {

    }


}