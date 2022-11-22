package com.example.hangmanapp.abductmania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import com.example.hangmanapp.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.progressBar.visibility = View.INVISIBLE

        binding.doRegisterButton.setOnClickListener() {
            // Register new user
            val email = binding.emailInputEditText.text.toString()
            val username = binding.userInputEditText.text.toString()
            val password = binding.passwordInputEditText.text.toString()

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {
                    binding.progressBar.visibility = View.VISIBLE

                    if (it.isSuccessful) {
                        // Good registration
                        binding.progressBar.visibility = View.INVISIBLE
                        val firebaseUser = firebaseAuth.currentUser

                        Toast.makeText(this, firebaseUser.toString(), Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, MainMenuActivity::class.java)
                        startActivity(intent)
                    } else {
                        // Error
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.backLoginButton.setOnClickListener() {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}