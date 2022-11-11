package com.example.hangmanapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.hangmanapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        //firebaseAuth.startActivityForSignInWithProvider(this, OAuthProvider.newBuilder())

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

            firebaseAuth.signInWithEmailAndPassword(username,password)
                .addOnSuccessListener()
            {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)

                finish()
            }.addOnFailureListener()
            {
                Toast.makeText(this, getString(R.string.errorUsernameOrPassword), Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun startAnimation()
    {

    }


}