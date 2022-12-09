package com.udacity.project4.locationreminders

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.udacity.project4.R
import com.udacity.project4.databinding.ActivityReminderDescriptionBinding
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.local.LocalDB
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import java.util.*

/**
 * Activity that displays the reminder details after the user clicks on the notification
 */
class ReminderDescriptionActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private val TAG = ReminderDescriptionActivity::class.java.simpleName
    private lateinit var reminderItem: ReminderDataItem

    companion object {
        private const val EXTRA_ReminderDataItem = "EXTRA_ReminderDataItem"

        //        receive the reminder object after the user clicks on the notification
        fun newIntent(context: Context, reminderDataItem: ReminderDataItem): Intent {
            val intent = Intent(context, ReminderDescriptionActivity::class.java)
            intent.putExtra(EXTRA_ReminderDataItem, reminderDataItem)
            return intent
        }
    }

    private lateinit var binding: ActivityReminderDescriptionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_reminder_description
        )

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.small_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

//        TODO: Add the implementation of the reminder details
        reminderItem = intent.extras?.getSerializable(EXTRA_ReminderDataItem) as ReminderDataItem
        binding.reminderDataItem = reminderItem
        binding.textCoordinates.text = "Latitude :" + reminderItem.latitude.toString() + "\nLongitude : " + reminderItem.longitude.toString()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val latitude = binding.reminderDataItem?.latitude
        val longitude = binding.reminderDataItem?.longitude
        val zoomLevel = 18f
        val reminderLatLng = LatLng(latitude!!,longitude!!)
        val reminderSnippet = String.format(
            Locale.getDefault(),
            "Lat: %1$.5f, Long: %2$.5f",
            reminderLatLng.latitude,
            reminderLatLng.longitude
        )

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(reminderLatLng, zoomLevel))
        map.addMarker(
            MarkerOptions().position(reminderLatLng).title(reminderItem.title).snippet(reminderSnippet).icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))

        setMapStyle(map)
    }

    private fun setMapStyle(map: GoogleMap) {
        try {
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this,
                    R.raw.map_style
                )
            )

            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }
}
