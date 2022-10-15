package com.example.hangmanapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hangmanapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonToast?.setOnClickListener {
            Toast.makeText(this, "This is a toast messege", Toast.LENGTH_LONG).show()
        }

        binding.floatingButtonStonks?.setOnClickListener {
            Snackbar.make(it, "Your messege was sent", Snackbar.LENGTH_LONG)
                .setAction("Undo") {

                }.show()
        }

    }
}