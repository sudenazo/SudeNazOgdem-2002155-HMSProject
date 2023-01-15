package com.example.sudenaz2002155

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hms.common.ApiException
import com.huawei.hms.location.*
import com.huawei.hms.support.feature.service.AuthService
//import androidx.synthetic.main.search_screen.*

class SearchScreen(private val context: Context) : AppCompatActivity() {

    private lateinit var auth: AuthService
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_screen)

      //  auth = AuthService()

        // Set up logout menu
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Display user information from Auth Service
        val userInfo = auth.getUserInfo()
        val userInfoTextView = findViewById<TextView>(R.id.user_info)
        userInfoTextView.text = userInfo.toString()

        // Set up country code spinner
        val countryCodeSpinner = findViewById<Spinner>(R.id.country_code_spinner)
        val countryCodes = arrayOf("US", "CN", "UK", "FR", "DE")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countryCodes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        countryCodeSpinner.adapter = adapter

        // Get distance from user
        val distanceEditText = findViewById<EditText>(R.id.distance)

        // Get user's location
        locationClient = LocationServices.getFusedLocationProviderClient(context)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                if (locationResult?.locations.isNullOrEmpty()) {
                    return
                }
                val location = locationResult?.locations?.get(0)
                val locationTextView = findViewById<TextView>(R.id.location)
                if (location != null) {
                    locationTextView.text = "Latitude: ${location.latitude}, Longitude: ${location.longitude}"
                }
            }
        }
        val locationButton = findViewById<ImageButton>(R.id.location_button)
        locationButton.setOnClickListener {
            val locationRequest = LocationRequest()
            locationRequest.interval = 10000
            locationRequest.fastestInterval = 5000
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationClient.requestLocationUpdates(locationRequest, locationCallback, null)


            }
            }

    private fun setSupportActionBar(toolbar: Toolbar?) {

    }


}

private fun AuthService.getUserInfo() {

}






