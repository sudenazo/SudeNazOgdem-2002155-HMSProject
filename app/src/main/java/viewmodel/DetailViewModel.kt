package viewmodel

import android.speech.tts.TextToSpeechService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sudenaz2002155.ChargingStation


class DetailViewModel : ViewModel() {
        private val ttsService: TextToSpeechService = TextToSpeechService()
        private val mapService: MapService = MapService()
        val chargingStation = MutableLiveData<ChargingStation>()

        fun loadChargingStation(id: String) {
            chargingStation.postValue(mapService.getChargingStation(id))
        }

        fun playAddress() {
            ttsService.play(chargingStation.value?.address)
        }
    }

