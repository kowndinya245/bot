package com.kv.bot.notification


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.RemoteViews
import com.kv.bot.ChatActivity
import com.kv.bot.R
import com.kv.bot.models.NotificationModel
import java.text.SimpleDateFormat
import java.util.*


class NotificationDecorator(private val context: Context) {
    private val CHANNEL_ID = "my_channel_id"
    private val CHANNEL_NAME = "My Chat Bot"
    private var NOTIFICATION_ID = 1

    init {
    }

    fun showNotification(title: String, message: String, data:Bundle) {


        val avatar = data.getInt(NotificationModel.CHAT_AVATAR)

        NOTIFICATION_ID++

        val intent = Intent(context, ChatActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        val dateFormat = SimpleDateFormat("MMM d, yyyy @ hh:mm a", Locale.getDefault())


        val remoteViews = RemoteViews(context.packageName, R.layout.custom_notification_layout)

        remoteViews.setTextViewText(R.id.textView1, title)
        remoteViews.setTextViewText(R.id.textView2, message)
        remoteViews.setImageViewResource(R.id.avatar, avatar)
        remoteViews.setTextViewText(R.id.date_text, dateFormat.format(System.currentTimeMillis()))

        var notificationManager:NotificationManager? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH // Importance level for the notification channel
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            notificationManager =  context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, CHANNEL_ID)
        } else {
            Notification.Builder(context)
        }


        notificationBuilder.setSmallIcon(avatar)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setCustomContentView(remoteViews)
            .setCustomBigContentView(remoteViews)
            .setPriority(Notification.PRIORITY_DEFAULT)

        notificationManager = notificationManager ?: context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

}