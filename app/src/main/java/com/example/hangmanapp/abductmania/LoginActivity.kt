package com.example.hangmanapp.abductmania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.hangmanapp.R
import com.example.hangmanapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity()
{
    private val EMAIL = "email"

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        if (firebaseAuth.currentUser != null)
        {
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
            finish()
        }


        binding.loadingBar.visibility = View.INVISIBLE

        binding.loadingBar.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus)
            {
                val username = binding.emailInputEditText.text.toString()
                if (!Patterns.EMAIL_ADDRESS.matcher(username).matches())
                {
                    binding.emailInputEditText.error = "Invalid username"
                }
                else
                {
                    binding.emailInputEditText.error = null
                }
            }
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailInputEditText.text.toString()

            val password = binding.passwordInputEditText.text.toString()

            binding.loginButton.setTextColor(getColor(R.color.purple_dark)) // Show darker color when held

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {

                    savedSharedPrefsLogedUser(email)

                    val intent = Intent(this@LoginActivity, MainMenuActivity::class.java)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, getString(R.string.errorUsernameOrPassword), Toast.LENGTH_LONG).show()
                }

            binding.loginButton.setTextColor(getColor(R.color.green_soft)) // Return color to normal
        }

        binding.registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.guestButton.setOnClickListener {
            firebaseAuth.signInAnonymously()
                .addOnSuccessListener {
                    val intent = Intent(this, MainMenuActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun savedSharedPrefsLogedUser(email : String)
    {
        val shared = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = shared.edit()
        editor.putString(EMAIL, email)
        editor.apply()
    }

}