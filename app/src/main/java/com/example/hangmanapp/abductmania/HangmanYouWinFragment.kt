package com.example.hangmanapp.abductmania

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hangmanapp.databinding.FragmentHangmanYouWinBinding

class HangmanYouWinFragment : Fragment()
{
    private lateinit var binding: FragmentHangmanYouWinBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) : View?
    {
        binding = FragmentHangmanYouWinBinding.inflate(inflater, container, false)


        binding.homeIcon.setOnClickListener {
            val intent = Intent(activity, MainMenuActivity::class.java)
            startActivity(intent)
        }

        binding.replayIcon.setOnClickListener {
            activity?.finish()
            //overridePendingTransition(0, 0);
            startActivity(activity?.intent);
            //overridePendingTransition(0, 0);
        }


        return binding.root
    }

    public fun Init(hangmanWord : String, score : Int)
    {
        binding.hangmanWordText.text = hangmanWord
        binding.scoreText.text = score.toString()
    }



}