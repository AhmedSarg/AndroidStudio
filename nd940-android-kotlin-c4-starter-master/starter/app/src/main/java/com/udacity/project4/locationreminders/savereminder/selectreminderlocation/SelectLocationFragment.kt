package com.udacity.project4.locationreminders.savereminder.selectreminderlocation


import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.LocationManager
import android.location.LocationProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.udacity.project4.R
import com.udacity.project4.base.BaseFragment
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.databinding.FragmentSelectLocationBinding
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.utils.setDisplayHomeAsUpEnabled
import org.koin.android.ext.android.inject
import java.lang.Exception
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom

import com.google.android.gms.maps.model.CameraPosition

import com.google.android.gms.maps.model.MarkerOptions

import com.google.android.gms.maps.model.LatLng

import com.google.android.gms.maps.GoogleMap

import com.google.android.gms.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.fragment_select_location.*
import java.util.*
import android.location.Criteria
import android.location.Location
import android.telephony.CarrierConfigManager
import androidx.appcompat.app.AppCompatActivity

import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.maps.CameraUpdate

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task


class SelectLocationFragment : BaseFragment(), OnMapReadyCallback {

    //Use Koin to get the view model of the SaveReminder
    override val _viewModel: SaveReminderViewModel by inject()
    private lateinit var binding: FragmentSelectLocationBinding
    private lateinit var map: GoogleMap
    private val REQUEST_LOCATION_PERMISSION = 1
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val TAG = SelectLocationFragment::class.java.simpleName
    private var lastMarker: Marker? = null
    private var str: String = ""
    private lateinit var poiMark: PointOfInterest
    private var lat: Double = 0.0
    private var lng: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_select_location, container, false)

        binding.viewModel = _viewModel
        binding.lifecycleOwner = this

        setHasOptionsMenu(true)
        setDisplayHomeAsUpEnabled(true)
        binding.button.text = getString(R.string.select_poi)
//      TODO: add the map setup implementation
        val mapView = binding.mapView
        //val mapView = requireActivity().supportFragmentManager.findFragmentById(R.id.map)?.map as SupportMapFragment
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)
//      TODO: zoom to the user location after taking his permission
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

//      TODO: call this function after the user confirms on the selected location
        binding.button.setOnClickListener {
            if (lastMarker != null)
                onLocationSelected()
        }
        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        enableMyLocation()
        goToDeviceLocation()
//      TODO: add style to the map
        setMapStyle(map)
        /*setLongClick(map)*/
//      TODO: put a marker to location that the user selected
        setPoiClick(map)
    }

    private fun onLocationSelected() {
        //        TODO: When the user confirms on the selected location,
        //         send back the selected location details to the view model
        //         and navigate back to the previous fragment to save the reminder and add the geofence
        _viewModel.selectedPOI.value = poiMark
        _viewModel.reminderSelectedLocationStr.value = str
        _viewModel.latitude.value = lat
        _viewModel.longitude.value = lng
        findNavController().popBackStack()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        // TODO: Change the map type based on the user's selection.
        R.id.normal_map -> {
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.hybrid_map -> {
            map.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }
        R.id.satellite_map -> {
            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.terrain_map -> {
            map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            ) === PackageManager.PERMISSION_GRANTED
        ) {
            map.setMyLocationEnabled(true)
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.size > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            }
        }
    }

    private fun goToDeviceLocation() {
        try {
            val locationResult: Task<Location> = fusedLocationProviderClient.getLastLocation()
            locationResult.addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Set the map's camera position to the current location of the device.
                    val location: Location = task.getResult()!!
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    val update = newLatLngZoom(currentLatLng,18f)
                    map.moveCamera(update)
                }
            })
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message!!)
        }
    }

    private fun setMapStyle(map: GoogleMap) {
        try {
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    R.raw.map_style
                )
            )
            if (!success) {
                Log.e(TAG, "Style Parsing Failed")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style")
        }
    }

    /*private fun setLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            if (lastMarker != null) {
                lastMarker?.remove()
            }
            val marker = map.addMarker(
                MarkerOptions().title("Dropped Pin").position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            )
            marker.showInfoWindow()
            lastMarker = marker
            str = marker.title
            lat = marker.position.latitude
            lng = marker.position.longitude
            binding.button.text = getString(R.string.save)
        }
    }*/


    private fun setPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener { poi ->
            if (lastMarker != null) {
                lastMarker?.remove()
            }
            val poiMarker = map.addMarker(
                MarkerOptions().position(poi.latLng).title(poi.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            )
            poiMarker.showInfoWindow()
            str = poiMarker.title
            lat = poiMarker.position.latitude
            lng = poiMarker.position.longitude
            poiMark = poi
            lastMarker = poiMarker
            binding.button.text = getString(R.string.save)
        }
    }

}
