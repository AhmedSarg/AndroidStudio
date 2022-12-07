package com.udacity.project4.locationreminders.geofence

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.data.local.LocalDB
import com.udacity.project4.locationreminders.data.local.RemindersDao
import com.udacity.project4.locationreminders.data.local.RemindersDatabase
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.utils.justSend
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

private const val TAG = "JobIntentService"

class GeofenceTransitionsJobIntentService : JobIntentService(), CoroutineScope {

    private var coroutineJob: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + coroutineJob

    companion object {
        private const val JOB_ID = 573

        //        TODO: call this to start the JobIntentService to handle the geofencing transition events
        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(
                context,
                GeofenceTransitionsJobIntentService::class.java,
                JOB_ID,
                intent
            )
            Log.i("ahmed", "enqueue")
        }
    }

    override fun onHandleWork(intent: Intent) {
        //TODO: handle the geofencing transition events and
        // send a notification to the user when he enters the geofence area
        //TODO call @sendNotification
        Log.i("ahmed", "on handle")
        val geofencingEvent = GeofencingEvent.fromIntent(intent)

        if (geofencingEvent.hasError()) {
            Log.i(TAG, "error")
            return
        } else {
            Log.i("ahmed", "no error")
            if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                Log.i("ahmed", "entered area")
                sendNotification(geofencingEvent.triggeringGeofences)
            }
        }
    }

    //TODO: get the request id of the current geofence
    private fun sendNotification(triggeringGeofences: List<Geofence>) {
        triggeringGeofences.forEach {
            val requestId = it.requestId
            Log.i("ahmed", "send noti " + requestId.toString())
            //Get the local repository instance
            val remindersLocalRepository: ReminderDataSource by inject()
            //val dao = LocalDB.createRemindersDao(this@GeofenceTransitionsJobIntentService)
            //val remindersLocalRepository = RemindersLocalRepository(dao)
//        Interaction to the repository has to be through a coroutine scope
            CoroutineScope(coroutineContext).launch(SupervisorJob()) {
                Log.i("ahmed", "check 1")
                //get the reminder with the request id
                val result = remindersLocalRepository.getReminder(requestId)
                //val result = remindersLocalRepository.getReminder(requestId)
                Log.i("ahmed", result.toString())
                Log.i("ahmed", requestId)
                if (result is Result.Success<ReminderDTO>) {
                    Log.i("ahmed", "check 2")
                    val reminderDTO = result.data
                    //send a notification to the user with the reminder details
                    com.udacity.project4.utils.sendNotification(
                        this@GeofenceTransitionsJobIntentService, ReminderDataItem(
                            reminderDTO.title,
                            reminderDTO.description,
                            reminderDTO.location,
                            reminderDTO.latitude,
                            reminderDTO.longitude,
                            reminderDTO.id
                        )
                    )
                }
            }
        }
    }

}
