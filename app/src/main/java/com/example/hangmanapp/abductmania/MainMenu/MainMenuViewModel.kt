package com.example.hangmanapp.abductmania.MainMenu

import android.content.Context
import android.media.MediaPlayer
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.example.hangmanapp.R
import com.example.hangmanapp.abductmania.Config.ConfigurationViewModel
import com.example.hangmanapp.abductmania.Game.HangmanGameViewModel
import com.example.hangmanapp.abductmania.Notifications.NotificationCreator
import com.example.hangmanapp.abductmania.Notifications.NotificationReceiver
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

class MainMenuViewModel : ViewModel()
{
    private lateinit var firebaseAuth: FirebaseAuth
    private val notificationCreator : NotificationCreator = NotificationCreator()


    companion object {
        const val CHANNEL_ID = "NOTIFICATIONS_CHANNEL_CONTACTS"
        var menuMusicMP : MediaPlayer? = null
        var gameMusicMP : MediaPlayer? = null
        var buttonSfxMP : MediaPlayer? = null

        var goingToAnotherMenu : Boolean = false
    }

    public fun init(context : Context, configurationViewModel : ConfigurationViewModel)
    {
        firebaseAuth = FirebaseAuth.getInstance()

        initAudios(context, configurationViewModel)
        initNotifications(context)

        menuMusicMP?.start()
    }

    private fun initAudios(context : Context, configurationViewModel : ConfigurationViewModel)
    {
        // Music
        menuMusicMP = MediaPlayer.create(context, R.raw.menus_song)
        menuMusicMP?.isLooping = true
        gameMusicMP = MediaPlayer.create(context, R.raw.game_song)
        gameMusicMP?.isLooping = true

        // SFX
        buttonSfxMP = MediaPlayer.create(context, R.raw.button_click)
        HangmanGameViewModel.victorySfxMP = MediaPlayer.create(context, R.raw.victory)
        HangmanGameViewModel.gameOverSfxMP = MediaPlayer.create(context, R.raw.game_over)
        HangmanGameViewModel.correctLetterSfxMP = MediaPlayer.create(context, R.raw.correct_letter)
        HangmanGameViewModel.abductorSfxMP = MediaPlayer.create(context, R.raw.ufo_abductor)
        HangmanGameViewModel.appearSfxMP = MediaPlayer.create(context, R.raw.ufo_appear)
        HangmanGameViewModel.buildingSfxMP = MediaPlayer.create(context, R.raw.ufo_building)

        // Reload
        configurationViewModel.reloadAudios(context)
    }

    private fun initNotifications(context : Context)
    {
        val shared = PreferenceManager.getDefaultSharedPreferences(context)
        val notificationsEnabled = shared.getBoolean(ConfigurationViewModel.NOTIFICATIONS, false)

        if (notificationsEnabled)
        {
            notificationCreator.createNotificationChannel(context)
            notificationCreator.createNotificationAlarm(context)
        }
        else
        {
            notificationCreator.stopNotificationAlarm(context)
        }
    }

    public fun signOut()
    {
        firebaseAuth.signOut()
    }

    public fun fireBaseNotification(context : Context)
    {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Toast.makeText(context,"Fetching FCM registration token failed", Toast.LENGTH_SHORT).show()
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            println(token)
            // Log and toast
            //val msg = "Token Recived!"
            //Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
        })
    }

}