package com.udacity

import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import com.udacity.reciever.OpenReciever

class ViewModel(private val app: Application) : AndroidViewModel(app) {

    private val REQUEST_CODE = 0

    private fun send(data: String) {
        val notificationManager = ContextCompat.getSystemService(
            app,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelAll()
    }

}