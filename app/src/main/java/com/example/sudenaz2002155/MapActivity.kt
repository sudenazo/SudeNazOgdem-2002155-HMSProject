
package com.example.sudenaz2002155

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.util.Log.d
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.tasks.Task
import com.huawei.hms.location.*
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
import com.huawei.hms.maps.utils.LogM

class MapActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding

    private lateinit var huaweiMap: HuaweiMap
    private lateinit var mapView:MapView
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var settingsClient: SettingsClient

    var mLocationRequest: LocationRequest? = null


    private var hMap: HuaweiMap? = null

    private var mMapView: MapView? = null

    companion object {
        private const val TAG = "MapActivity"
        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    }

    init {
        binding = ActivityMainBinding.inflate(cntx.layoutInflater)
        val view = binding.root
        val cntx = null
        cntx.setContentView(view)
        sharedPrefsManager = SharedPrefsManager(cntx)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingInflatedId")


    override fun onCreate(savedInstanceState: Bundle?) {
        LogM.d(TAG, "onCreate:")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapview_demo)
        mMapView = findViewById(R.id.mapView)
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle =
                savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        mMapView?.apply {
            onCreate(mapViewBundle)
            getMapAsync(this@MapActivity)
        }

        binding.mapLayout.mapMap.onCreate(mapViewBundle)
        binding.mapLayout.mapMap.getMapAsync(this)

        var mLocationCallback: LocationCallback? = null
        if (null == mLocationCallback) {
            mLocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    if (locationResult != null) {
                        val locations: List<Location> =
                            locationResult.locations
                        if (locations.isNotEmpty()) {
                            for (location in locations) {
                                Log.i(
                                    TAG,
                                    "onLocationResult location[Longitude,Latitude,Accuracy]:${location.longitude} , ${location.latitude} , ${location.accuracy}"
                                )
                            }
                        }
                    }
                }

                override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
                    locationAvailability?.let {
                        val flag: Boolean = locationAvailability.isLocationAvailable
                        Log.i(TAG, "onLocationAvailability isLocationAvailable:$flag")
                    }
                }
            }
        }
        fun requestLocationUpdatesWithCallback() {
            try {
                val builder = LocationSettingsRequest.Builder()
                builder.addLocationRequest(mLocationRequest)
                val locationSettingsRequest = builder.build()
                // Check the device settings before requesting location updates.
                val locationSettingsResponseTask: Task =
                    settingsClient.checkLocationSettings(locationSettingsRequest)

                locationSettingsResponseTask.addOnSuccessListener { locationSettingsResponse: LocationSettingsResponse? ->
                    Log.i(TAG, "check location settings success  {$locationSettingsResponse}")
                    // Request location updates.
                    fusedLocationProviderClient.requestLocationUpdates(
                        mLocationRequest,
                        mLocationCallback,
                        Looper.getMainLooper()
                    )
                        .addOnSuccessListener {
                            Log.i(TAG, "requestLocationUpdatesWithCallback onSuccess")
                        }
                        .addOnFailureListener { e ->
                            Log.e(
                                TAG,
                                "requestLocationUpdatesWithCallback onFailure:${e.message}"
                            )
                        }
                }
                    .addOnFailureListener { e: Exception ->
                        Log.e(TAG, "checkLocationSetting onFailure:${e.message}")
                    }
            } catch (e: Exception) {
                Log.e(TAG, "requestLocationUpdatesWithCallback exception:${e.message}")
            }

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                Log.i(TAG, "sdk < 28 Q")
                if (checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PERMISSION_GRANTED
                    && checkSelfPermission(
                        this,
                        ACCESS_COARSE_LOCATION
                    ) != PERMISSION_GRANTED
                ) {
                    val strings = arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        ACCESS_COARSE_LOCATION
                    )
                    requestPermissions(this, strings, 1)
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PERMISSION_GRANTED
                    && checkSelfPermission(
                        this,
                        ACCESS_COARSE_LOCATION
                    ) != PERMISSION_GRANTED
                ) {
                    val strings = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
                    ActivityCompat.requestPermissions(this, strings, 3)
                } else {
                    if (checkSelfPermission(
                            this,
                            "android.permission.ACCESS_BACKGROUND_LOCATION"
                        ) != PERMISSION_GRANTED
                    ) {
                        val permission = arrayOf("android.permission.ACCESS_BACKGROUND_LOCATION")
                        ActivityCompat.requestPermissions(this, permission, 0)
                    }
                }
            } else {
                if (checkSelfPermission(this@RequestLocationUpdatesWithCallbackActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PERMISSION_GRANTED && checkSelfPermission(
                        this@RequestLocationUpdatesWithCallbackActivity,
                        ACCESS_COARSE_LOCATION
                    ) != PERMISSION_GRANTED && checkSelfPermission(
                        this@RequestLocationUpdatesWithCallbackActivity,
                        "android.permission.ACCESS_BACKGROUND_LOCATION"
                    ) != PERMISSION_GRANTED
                ) {
                    val strings = arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        ACCESS_COARSE_LOCATION,
                        "android.permission.ACCESS_BACKGROUND_LOCATION"
                    )
                    requestPermissions(this, strings, 2)
                }
            }


        }

         fun removeLocationUpdatesWithCallback() {
            try {
                fusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
                    .addOnSuccessListener {
                        Log.i(
                            TAG,
                            "removeLocationUpdatesWithCallback onSuccess"
                        )
                    }
                    .addOnFailureListener { e ->
                        Log.e(
                            TAG,
                            "removeLocationUpdatesWithCallback onFailure:${e.message}"
                        )
                    }
            } catch (e: Exception) {
                Log.e(
                    TAG,
                    "removeLocationUpdatesWithCallback exception:${e.message}"
                )
            }
        }
    }
    override fun onStart() {
        super.onStart()
        mMapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mMapView?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView?.onDestroy()
    }

    override fun onPause() {
        mMapView?.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mMapView?.onResume()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size > 1 && grantResults[0] == PERMISSION_GRANTED && grantResults[1] == PERMISSION_GRANTED
            ) {
                Log.i(
                    TAG, "onRequestPermissionsResult: apply LOCATION PERMISSION successful"
                )
            } else {
                Log.i(
                    TAG, "onRequestPermissionsResult: apply LOCATION PERMISSION  failed"
                )
            }
        }
        if (requestCode == 2) {
            if (grantResults.size > 2 && grantResults[2] == PERMISSION_GRANTED && grantResults[0] == PERMISSION_GRANTED && grantResults[1] == PERMISSION_GRANTED
            ) {
                Log.i(
                    TAG, "onRequestPermissionsResult: apply ACCESS_BACKGROUND_LOCATION successful"
                )
            } else {
                Log.i(
                    TAG, "onRequestPermissionsResult: apply ACCESS_BACKGROUND_LOCATION  failed"
                )
            }
        }
        if (requestCode == 3) {
            if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED
                || grantResults[1] == PERMISSION_GRANTED
            ) {
                if (checkSelfPermission(
                        this,
                        "android.permission.ACCESS_BACKGROUND_LOCATION"
                    ) != PERMISSION_GRANTED
                ) {
                    val permission = arrayOf("android.permission.ACCESS_BACKGROUND_LOCATION")
                    ActivityCompat.requestPermissions(this, permission, 0)
                }
            } else {
                Log.i(
                    TAG,
                    "onRequestPermissionsResult: apply LOCATION PERMISSSION  failed"
                )
            }
        }
    }

    private fun removeLocationUpdatesWithCallback() {
        try {
            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
                .addOnSuccessListener {
                    Log.i(
                        TAG,
                        "removeLocationUpdatesWithCallback onSuccess"
                    )
                }
                .addOnFailureListener { e ->
                    Log.e(
                        TAG,
                        "removeLocationUpdatesWithCallback onFailure:${e.message}"
                    )
                }
        } catch (e: Exception) {
            Log.e(
                TAG,
                "removeLocationUpdatesWithCallback exception:${e.message}"
            )
        }
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }


    private fun requestPermissions(mapActivity: MapActivity, strings: Array<String>, i: Int) {

    }

    private fun checkSelfPermission(mapActivity: MapActivity, accessFineLocation: String): Int {

    }

    private fun getMapAsync(mapActivity: MapActivity) {

    }

    fun onMapReady(map: HuaweiMap) {
        Log.d(TAG, "onMapReady: ")
        hMap = map
    }

}
