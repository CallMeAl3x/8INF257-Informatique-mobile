package ca.uqac.stories.presentation.components

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import ca.uqac.stories.data.source.StoriesDatabase
import java.util.Calendar
import kotlin.random.Random
import ca.uqac.stories.R
import android.util.Log
import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AlarmReceiver", "Alarm triggered")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val dao = StoriesDatabase.getInstance(context).dao
                val currentStories = dao.getStoriesByTime(
                    Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                    Calendar.getInstance().get(Calendar.MINUTE)
                )

                currentStories.forEach { story ->
                    showNotification(
                        context,
                        "Rappel : ${story.title}",
                        story.description
                    )
                }
            } catch (e: Exception) {
                Log.e("AlarmReceiver", "Erreur lors de la récupération des stories: ${e.message}")
            }
        }
    }

    private fun showNotification(context: Context, title: String, message: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "reminder_channel"
        val channelName = "Rappels"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }
}