package com.example.hangmanapp.abductmania.Notifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.hangmanapp.R

class NotificationCreator
{
    private lateinit var alarmManager : AlarmManager

    companion object
    {
        public fun getPlayNotification(context : Context, intent: Intent?) : NotificationCompat.Builder
        {
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            val notificationBuilder = NotificationCompat.Builder(context, NotificationReceiver.DAILY_REMINDER_CHANNEL_ID)
                .setSmallIcon(R.drawable.abductmania_icon)
                .setContentTitle("Play AbductMania!")
                .setContentText("The city is getting attacked! Come fight the aliens with... ehem... words")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)

            return notificationBuilder
        }
    }



    public fun createNotificationChannel(context : Context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Daily Plant reminder"
            val descriptionText = "Reminder to Plant every day"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NotificationReceiver.DAILY_REMINDER_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    public fun createNotificationAlarm(context : Context)
    {
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, NotificationReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, 0, 1000, pendingIntent)
    }


}