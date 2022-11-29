package com.example.hangmanapp.abductmania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.hangmanapp.R
import com.example.hangmanapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private val USERS_COLLECTION = "users"
    private val EMAIL = "email"

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private var canRegister = true//false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.progressBar.visibility = View.INVISIBLE

        emailFocusListener()
        usernameFocusListener()
        passwordFocusListener()

        binding.doRegisterButton.setOnClickListener {
            // Register new user
            val email = binding.emailInputEditText.text.toString()
            val username = binding.userInputEditText.text.toString()
            val password = binding.passwordInputEditText.text.toString()

            submitForm()

            if (canRegister) {
                binding.progressBar.visibility = View.VISIBLE

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) {
                        binding.progressBar.visibility = View.INVISIBLE

                        if (it.isSuccessful) {
                            // Good registration
                            savedSharedPrefsRegisteredUser(email)

                            fireStoreRegisterUser(username, email)

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

        binding.backLoginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun submitForm() {
        val validEmail = binding.emailInputLayout.helperText == null
        val validUser = binding.usernameInputLayout.helperText == null
        val validPassword = binding.passwordInputLayout.helperText == null

        canRegister = validEmail && validUser && validPassword
    }

    // EMAIL
    private fun emailFocusListener() {
        binding.emailInputEditText.setOnFocusChangeListener { view, focused ->
            if (!focused) {
                binding.emailInputLayout.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String? {
        val emailText = binding.emailInputEditText.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return "Invalid Email Address"
        }

        return null
    }

    // USERNAME
    private fun usernameFocusListener() {
        binding.userInputEditText.setOnFocusChangeListener { view, focused ->
            if (!focused) {
                binding.usernameInputLayout.helperText = validUsername()
            }
        }
    }

    private fun validUsername(): String? {
        val userText = binding.userInputEditText.text.toString()
        if (userText.length < 2) {
            return "Minimum 2 Characters Username"
        }

        return null
    }

    // PASSWORD
    private fun passwordFocusListener() {
        binding.passwordInputEditText.setOnFocusChangeListener { view, focused ->
            if (!focused) {
                binding.passwordInputLayout.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String? {
        val passwordText = binding.passwordInputEditText.text.toString()
        if (passwordText.length < 8) {
            return "Minimum 8 Characters Password"
        }
        if (!passwordText.matches(".*[A-Z].*".toRegex())) {
            return "Must Contain 1 Upper-case Character"
        }
        if (!passwordText.matches(".*[a-z].*".toRegex())) {
            return "Must Contain 1 Lower-case Character"
        }
        if (!passwordText.matches(".*[|@#€¬!·$%&/()=?¿¡^*¨´`+{}_:;<>].*".toRegex())) {
            return "Must Contain 1 Special Character"
        }

        return null
    }

    private fun savedSharedPrefsRegisteredUser(email : String)
    {
        val shared = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = shared.edit()
        editor.putString(EMAIL, email)
        editor.apply()
    }

    private fun fireStoreRegisterUser(username : String, email :String)
    {
        val usersCollection = firestore.collection(USERS_COLLECTION)
        val user = User(username, email)

        usersCollection.document(user.username).set(user)
    }

}