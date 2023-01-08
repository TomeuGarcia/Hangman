package com.example.hangmanapp.abductmania.Notifications

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.hangmanapp.R

class NotificationReceiver : BroadcastReceiver()
{

    companion object{
        val CONFIGURATION_PREFS = "configPrefs"
        val NOTIFICATIONS = "notifications"
        val DAILY_REMINDER_CHANNEL_ID = "DAILY_REMINDER"
    }

    override fun onReceive(context: Context?, intent: Intent?)
    {
        val sharedPreference =  context?.getSharedPreferences(CONFIGURATION_PREFS, Context.MODE_PRIVATE)
        val notificationExists = sharedPreference?.getBoolean(NOTIFICATIONS, true) ?: true
        if (!notificationExists){
            return
        }

        context?.apply {
            val playNotificationBuilder = NotificationCreator.getPlayNotification(context, intent)
            NotificationManagerCompat.from(context).notify(1, playNotificationBuilder.build())
        }

    }

}