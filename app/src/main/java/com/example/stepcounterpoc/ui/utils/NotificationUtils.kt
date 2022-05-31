package com.example.stepcounterpoc.ui.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.stepcounterpoc.R
import com.example.stepcounterpoc.R.string
import com.example.stepcounterpoc.ui.main.MainActivity
import com.example.stepcounterpoc.ui.utils.StepCounterService.ActionListener
import com.example.stepcounterpoc.ui.utils.StepCounterService.Companion.KEY_NOTIFICATION_ID
import com.example.stepcounterpoc.ui.utils.StepCounterService.Companion.KEY_NOTIFICATION_STOP_ACTION


const val NOTIFICATION_ID = 1
private const val REQUEST_CODE = 0
private const val STOP_REQUEST_CODE = 2

fun createNotification(messageBody: String, context: Context): Notification {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            context.getString(string.step_counter_notification_channel_id),
            context.getString(string.step_notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply { setShowBadge(false) }

        notificationChannel.enableLights(false)
        notificationChannel.enableVibration(false)
        notificationChannel.description =
            context.getString(R.string.step_counter_channel_description)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }

    val builder = NotificationCompat.Builder(
        context,
        context.getString(R.string.step_counter_notification_channel_id)
    )
        .setSmallIcon(R.drawable.ic_baseline_directions_walk_24)
        .setDefaults(Notification.DEFAULT_ALL)
        .setContentTitle(context
            .getString(R.string.notification_title))
        .setContentText(messageBody)
        .setWhen(System.currentTimeMillis())
        .setDefaults(0)

        .setContentIntent(PendingIntent.getActivity(
            context,
            REQUEST_CODE,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_MUTABLE,
        ))

        .setAutoCancel(true)

        .setSilent(true)

        .setStyle(NotificationCompat.BigPictureStyle()
            .bigPicture(BitmapFactory.decodeResource(
                context.resources,
                R.drawable.ic_baseline_directions_walk_24
            ))
            .bigLargeIcon(null))
        .setLargeIcon(BitmapFactory.decodeResource(
            context.resources,
            R.drawable.ic_baseline_directions_walk_24
        ))

        // Add stop action
        .addAction(
            R.drawable.ic_baseline_directions_walk_24,
            context.getString(R.string.stop_action),
            PendingIntent.getBroadcast(context,
            STOP_REQUEST_CODE,
            Intent(context, ActionListener::class.java).run {
                action = KEY_NOTIFICATION_STOP_ACTION
                putExtra(KEY_NOTIFICATION_ID, NOTIFICATION_ID)
            },
            PendingIntent.FLAG_MUTABLE)
        )

        .setPriority(NotificationCompat.PRIORITY_LOW)
    return builder.build()
}