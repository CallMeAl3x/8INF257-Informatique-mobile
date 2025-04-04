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

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val dao = StoriesDatabase.getInstance(context).dao

        // Récupérer toutes les stories avec heure/minutes définies
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
    }

    private fun showNotification(context: Context, title: String, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            "reminder_channel",
            "Rappels",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, "reminder_channel")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }
}