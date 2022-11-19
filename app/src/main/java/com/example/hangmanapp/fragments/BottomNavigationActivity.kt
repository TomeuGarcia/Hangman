package com.example.hangmanapp.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.hangmanapp.R
import com.example.hangmanapp.databinding.ActivityBottomNavigationBinding

class BottomNavigationActivity : AppCompatActivity()
{
    private lateinit var binding : ActivityBottomNavigationBinding


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val numbersRound = FirstRoundFragment()
        val colorsRound = SecondRoundFragment()



        /*
        binding.checkButton.setOnClickListener {
            if (numbersRound.checkRound())
            {
                Toast.makeText(this, "Thats right", Toast.LENGTH_LONG).show()
            }
            else
            {
                Toast.makeText(this, "Wrong", Toast.LENGTH_LONG).show()
            }
        }
        */

        changeFragment(colorsRound)

        binding.bottomNavView.setOnItemSelectedListener {
            when(it.itemId)
            {
                R.id.colorsButton -> changeFragment(colorsRound)
                R.id.numbersButton -> changeFragment(numbersRound)
                R.id.quotesButton -> Toast.makeText(this, "Not implemented", Toast.LENGTH_LONG).show()
            }

            true
        }
    }

    private fun changeFragment(fragment: Fragment)
    {

        supportFragmentManager.beginTransaction().apply{
            supportFragmentManager.fragments.forEach {
                hide(it)
            }

            if (fragment.isAdded)
            {
                show(fragment)
            }
            else
            {
                replace(binding.fragmentContainer.id, fragment)
            }
            commit()
        }
    }


}