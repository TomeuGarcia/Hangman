package com.example.hangmanapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hangmanapp.databinding.FragmentFirstRoundBinding

class FirstRoundFragment : Fragment()
{
    private lateinit var binding : FragmentFirstRoundBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstRoundBinding.inflate(inflater, container, false)

        return binding.root
    }

    public fun checkRound() : Boolean
    {
        return binding.switch1.isChecked && !binding.switch2.isChecked && binding.switch4.isChecked
    }

}