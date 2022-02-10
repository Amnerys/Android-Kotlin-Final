package com.udacity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder

fun sendNotification(
    mContext: Context,
    data: Long?,
    selectedOption: String
) {

    val intent = Intent(mContext, DetailActivity::class.java)
    intent.action = "See changes"
    intent.putExtra(MainActivity.DOWNLOAD_ID, data)
    intent.putExtra(MainActivity.SELECTED_FILE, selectedOption)

    val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(mContext).run {
        // Add the intent, which inflates the back stack
        addNextIntentWithParentStack(intent)

        // Get the PendingIntent containing the entire back stack
        getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val notificationBuild = NotificationCompat.Builder(mContext, MainActivity.CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_assistant)
        .setContentTitle(mContext.resources.getString(R.string.notification_title))
        .setAutoCancel(true)
        .addAction(0, "See changes", resultPendingIntent)
        .setStyle(
            NotificationCompat.BigTextStyle()
                .bigText(mContext.resources.getString(R.string.notification_description))
        )
        .setSound(defaultSound)

    val notificationManager =
        mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            MainActivity.CHANNEL_ID,
            mContext.resources.getString(R.string.notification_title),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }
    notificationManager.notify(0, notificationBuild.build())
}
