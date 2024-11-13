package com.example.priority.view.main

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.priority.databinding.ActivityMainBinding
import com.example.priority.utils.OnSmoothBottomBarItemSelectedListener
import com.example.priority.view.profile.ProfileFragment
import com.example.priority.R
import com.example.priority.data.ResultState
import com.example.priority.view.calculator.CalculatorFragment
import com.example.priority.view.task.DetailFragment
import com.example.priority.view.task.TaskFragment
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), OnSmoothBottomBarItemSelectedListener,
    OnMapReadyCallback {

    private val viewModel by viewModels<DashboardViewModel>()

    private lateinit var binding: ActivityMainBinding
    private lateinit var locationRequest: LocationRequest
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createLocationRequest()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.bottomBar.setOnItemSelectedListener {
            when(it){
                0 -> getMyLastLocation()
                1 -> gantiFragment(DetailFragment())
                2 -> gantiFragment(CalculatorFragment())
                3 -> gantiFragment(ProfileFragment())
            }
        }
    }

    private fun gantiFragment(fragment: Fragment) {
        val transaksi = supportFragmentManager.beginTransaction()
        transaksi.replace(R.id.frame, fragment)
        transaksi.commit()
    }

    override fun onItemSelected(index: Int) {
        binding.bottomBar.itemActiveIndex=index
    }

    override fun onMapReady(p0: GoogleMap) {

    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                else -> {
                    Log.i("gadalokasi", "onActivityResult: All location settings are satisfied.")
                }
            }
        }

    private val resolutionLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            when (result.resultCode) {
                RESULT_OK ->
                    Log.i("lokasimaseh", "onActivityResult: All location settings are satisfied.")
                RESULT_CANCELED ->
                    Toast.makeText(
                        this@MainActivity,
                        "Anda harus mengaktifkan GPS untuk menggunakan aplikasi ini!",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(1)
            maxWaitTime = TimeUnit.SECONDS.toMillis(1)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        client.checkLocationSettings(builder.build())
            .addOnSuccessListener {
                getMyLastLocation()
            }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        resolutionLauncher.launch(
                            IntentSenderRequest.Builder(exception.resolution).build()
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        Toast.makeText(this@MainActivity, sendEx.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {

        if     (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    Log.d(
                        "koordinat",
                        "getMylastLocation: ${location.latitude}, ${location.longitude}  "
                    )
                    viewModel.getAqi(location.latitude,location.longitude,"7265fb06-74ce-469c-801d-3a99fd62093b").observe(this){result->
                        if (result != null) {
                            when (result) {
                                is ResultState.Loading -> {
//                                    showLoading(true)
                                }
                                is ResultState.Success -> {
                                    Log.d("resultdiactivity", "hasilAqiu:${result.data.data.current?.pollution?.aqius} ")
                                    val mFragmentManager = supportFragmentManager
                                    val mFragmentTransaction = mFragmentManager.beginTransaction()
                                    val mFragment = DashboardFragment()
                                    val mBundle = Bundle()
                                    mBundle.putString("textAqiu",result.data.data.current?.pollution?.aqius.toString())
                                    mBundle.putString("textCity",result.data.data.city.toString())
                                    mBundle.putString("textState",result.data.data.state.toString())
                                    mBundle.putString("textTime",result.data.data.current?.pollution?.ts.toString())
                                    mFragment.arguments = mBundle
                                    mFragmentTransaction.replace(R.id.frame, mFragment).commit()

                                }
                                is ResultState.Error -> {
//                                    showLoading(false)
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
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
}