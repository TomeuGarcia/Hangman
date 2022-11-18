package com.example.hangmanapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hangmanapp.databinding.FragmentFirstRoundBinding
import com.example.hangmanapp.databinding.FragmentSecondRoundBinding

class SecondRoundFragment : Fragment()
{
    private  lateinit var binding: FragmentSecondRoundBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSecondRoundBinding.inflate(inflater, container, false)

        return binding.root
    }

    public fun checkRound() : Boolean
    {
        return binding.correctRB.isChecked
    }

}