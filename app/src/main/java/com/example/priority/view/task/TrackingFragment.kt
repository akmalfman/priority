package com.example.priority.view.task

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.priority.R
import com.example.priority.databinding.FragmentTrackingBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class TrackingFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private var isTracking = false
    private lateinit var locationCallback: LocationCallback
    private var totalDistance = 0.0
    private var previousLocation: Location? = null
    private var allLatLng = ArrayList<LatLng>()
    private var boundsBuilder = LatLngBounds.Builder()
    private var trackingMarker: Marker? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackingBinding.inflate(inflater, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        binding.btnNext.visibility = View.GONE // Hide btnNext when starting

        // Set up map
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        getMyLastLocation()
        createLocationRequest()
        createLocationCallback()

        binding.btnStart.setOnClickListener {
            if (!isTracking) {
                clearMaps()
                updateTrackingStatus(true)
                startLocationUpdates()
            } else {
                updateTrackingStatus(false)
                stopLocationUpdates()
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> getMyLastLocation()
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> getMyLastLocation()
                else -> Toast.makeText(requireContext(), "Permission denied.", Toast.LENGTH_SHORT).show()
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    showStartMarker(location)
                } else {
                    Toast.makeText(requireContext(), "Location is not found. Try Again", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun showStartMarker(location: Location) {
        val startLocation = LatLng(location.latitude, location.longitude)
        mMap.addMarker(
            MarkerOptions()
                .position(startLocation)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_person_marker))
                .title(getString(R.string.start_point))
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 17f))
    }

    private val resolutionLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            when (result.resultCode) {
                AppCompatActivity.RESULT_OK -> Log.i(TAG, "onActivityResult: All location settings are satisfied.")
                AppCompatActivity.RESULT_CANCELED -> Toast.makeText(requireContext(), "Please enable GPS.", Toast.LENGTH_SHORT).show()
            }
        }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L).build()
        val client = LocationServices.getSettingsClient(requireActivity())
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        client.checkLocationSettings(builder.build())
            .addOnSuccessListener { getMyLastLocation() }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    resolutionLauncher.launch(
                        IntentSenderRequest.Builder(exception.resolution).build()
                    )
                }
            }
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    val lastLatLng = LatLng(location.latitude, location.longitude)
                    if (trackingMarker == null) {
                        trackingMarker = mMap.addMarker(
                            MarkerOptions()
                                .position(lastLatLng)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_person_marker))
                                .title("You are here")
                        )
                    } else {
                        trackingMarker?.position = lastLatLng
                    }

                    allLatLng.add(lastLatLng)
                    mMap.addPolyline(
                        PolylineOptions().color(Color.CYAN).width(10f).addAll(allLatLng)
                    )

                    boundsBuilder.include(lastLatLng)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 64))

                    previousLocation?.let {
                        totalDistance += it.distanceTo(location)
                    }
                    previousLocation = location
                }
            }
        }
    }

    private fun updateTrackingStatus(newStatus: Boolean) {
        isTracking = newStatus
        binding.btnStart.text = if (isTracking) getString(R.string.stop_running) else getString(R.string.start_running)
        binding.btnNext.visibility = if (isTracking) View.GONE else View.VISIBLE
        if (!isTracking) {
            val formattedDistance = String.format("%.3f", totalDistance / 1000)
            Toast.makeText(requireContext(), "Total distance: $formattedDistance km", Toast.LENGTH_LONG).show()
            val bundleDistance = totalDistance / 1000

            binding.btnNext.setOnClickListener {
                val bundle = Bundle()
                bundle.putDouble("distance", bundleDistance)

                val resultFragment = ResultFragment()
                resultFragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.frame, resultFragment)
                    .addToBackStack(null)
                    .commit()
            }
            totalDistance = 0.0
            previousLocation = null
        }
    }

    private fun startLocationUpdates() {
        try {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        } catch (exception: SecurityException) {
            Log.e(TAG, "Error : " + exception.message)
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun clearMaps() {
        mMap.clear()
        allLatLng.clear()
        boundsBuilder = LatLngBounds.Builder()
        trackingMarker = null
    }

    override fun onResume() {
        super.onResume()
        if (isTracking) startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "TrackingFragment"
    }
}
