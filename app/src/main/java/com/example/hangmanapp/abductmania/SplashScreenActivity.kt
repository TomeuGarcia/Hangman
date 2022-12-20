package com.example.hangmanapp.abductmania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hangmanapp.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity()
{
    private lateinit var binding: ActivitySplashScreenBinding

    private val START_LOGIN_DELAY_MILLISECONDS : Long = 2000


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()


        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startLoginActivity(START_LOGIN_DELAY_MILLISECONDS)
    }


    private fun startLoginActivity(delay: Long)
    {
        CoroutineScope(Dispatchers.Default).launch {
            delay(delay)

            val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}