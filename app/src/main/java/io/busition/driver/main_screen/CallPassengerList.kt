package io.busition.driver.main_screen

import androidx.lifecycle.ViewModel
import io.busition.driver.databinding.BusStartMainScreenBinding

interface CallPassengerList {
    fun callPassengerList(stationUuid: Int, passengerViewmodel:PassengerViewModel)
}