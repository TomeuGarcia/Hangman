package com.example.hangmanapp.abductmania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.widget.addTextChangedListener
import com.example.hangmanapp.R
import com.example.hangmanapp.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private var canRegister = true//false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.progressBar.visibility = View.INVISIBLE

        /*binding.emailInputEditText.addTextChangedListener (object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int){
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    binding.emailInputLayout.error = null
                    canRegister = true
                } else {
                    binding.emailInputLayout.error = "Invalid email"
                    canRegister = false
                }
            }

            override fun afterTextChanged(s: Editable) {
                TODO("Not yet implemented")
            }
        })

        binding.passwordInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                if (count > 6) {
                    binding.passwordInputLayout.error = null
                    canRegister = true
                } else {
                    binding.passwordInputLayout.error = "Password to short"
                    canRegister = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                TODO("Not yet implemented")
            }

        })*/

        binding.doRegisterButton.setOnClickListener() {
            // Register new user
            val email = binding.emailInputEditText.text.toString()
            val username = binding.userInputEditText.text.toString()
            val password = binding.passwordInputEditText.text.toString()

            if (canRegister) {
                binding.progressBar.visibility = View.VISIBLE

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) {
                        binding.progressBar.visibility = View.INVISIBLE

                        if (it.isSuccessful) {
                            // Good registration
                            val firebaseUser = firebaseAuth.currentUser

                            Toast.makeText(this, firebaseUser.toString(), Toast.LENGTH_SHORT).show()

                            val intent = Intent(this, MainMenuActivity::class.java)
                            startActivity(intent)
                        } else {
                            // Error
                            Toast.makeText(this, getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(this, getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backLoginButton.setOnClickListener() {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}