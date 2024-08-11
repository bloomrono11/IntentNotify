package com.lu.init;

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

/**
 * @author : bloomrono11@gmail.com
 * @company : Personal
 * created    : 8/10/2024, Saturday
 * @copyright : None
 */
class DisplayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disp)

        val tvReceivedMessage:EditText = findViewById(R.id.messageReceivedET)

        val message = intent.getStringExtra("MESSAGE_KEY")?: ""
        tvReceivedMessage.setText(message)

        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

            // Request the permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_CODE_POST_NOTIFICATIONS
            )
        } else {
            // Permission is already granted, proceed with creating the notification
            createNotification(message)
        }
    }

    private fun createNotification(message: String) {
        val channelId = "MESSAGE_CHANNEL"
        val notificationId = 101

        // Create an Intent for the activity you want to start
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent
            .getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Create Notification Channel for Android 8.0+

        val name = "MessageChannel"
        val descriptionText = "Channel for message notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)


        // Build the notification
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.rounded_notification_ic)
            .setContentTitle("New Message")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setLights(0xFF00FF00.toInt(), 300, 1000)
            .setVibrate(longArrayOf(0, 500, 1000))

        // Show the notification
        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, create the notification
                createNotification(intent.getStringExtra("MESSAGE_KEY") ?: "")
            } else {
                // Permission denied, handle accordingly (e.g., show a message or disable notification feature)
                // Optional: Notify the user that notifications won't work without the permission.
            }
        }
    }

    companion object{

        private const val REQUEST_CODE_POST_NOTIFICATIONS = 1
    }
}
