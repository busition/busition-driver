package io.busition.driver.main_screen

interface PassengerListListener {
    fun onToggleVisibility(isVisible: Boolean, stationUuid: Int)
}