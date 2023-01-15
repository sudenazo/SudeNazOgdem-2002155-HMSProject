package com.example.sudenaz2002155

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.getInstance
import com.huawei.hms.mlkit.tts.MLTtsCallback
import com.huawei.hms.mlkit.tts.MLTtsError
import java.util.*

class DetailActivity : AppCompatActivity() {

    private lateinit var chargingStationNameView: TextView
    private lateinit var chargingStationAddressView: TextView
    private lateinit var ttsEngine: MLTtsEngine

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        chargingStationNameView = findViewById(R.id.charging_station_name)
        chargingStationAddressView = findViewById(R.id.charging_station_address)

        val chargingStation = intent.getParcelableExtra<ChargingStation>("charging_station")
        if (chargingStation != null) {
            chargingStationNameView.text = chargingStation.name
        }
        if (chargingStation != null) {
            chargingStationAddressView.text = chargingStation.address
        }

        // Initialize the TTS engine.
        ttsEngine = MLTtsEngine.getInstance()
        ttsEngine.setLocale(Locale.US)
        ttsEngine.setVoice("en-US-ljz")
        ttsEngine.setPitch(1.0f)
        ttsEngine.setSpeechRate(1.0f)

        // Convert address information to speech.
        ttsEngine.speak(chargingStation.address, object : MLTtsCallback() {
            fun onError(p0: String?, p1: MLTtsError?) {
                // Handle error.
            }

            fun onComplete() {
                // Handle completion.
            }

            fun onStart() {
                // Handle start.
            }
        })
    }
}
