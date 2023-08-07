package io.busition.driver.roomDB

import android.content.Context
import androidx.room.Room

object DataManager {
    private lateinit var instance: DataManager
    private lateinit var appDatabase: AppDatabase


    fun getInstance(context: Context) {
        if (!::instance.isInitialized) {
            appDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "my_database").build()
            instance = DataManager
        }
    }

    fun clearDatabase() {
        appDatabase.clearAllTables()
    }

    fun addStation(stationUUID: String) {
        val station = Station(uuid = stationUUID)
        appDatabase.stationDao().insertStation(station)
    }

    fun addPassenger(stationUUID: String, passengerUUID: String, passengerName: String, passengerPhoneNumber: String) {
        val passenger = Passenger(uuid = passengerUUID, name = passengerName, phoneNumber = passengerPhoneNumber)
        appDatabase.passengerDao().insertPassenger(passenger)
    }


    fun getAllStations(): List<Station> {
        return appDatabase.stationDao().getAllStations()
    }

    fun getAllPassengers(): List<Passenger> {
        return appDatabase.passengerDao().getAllPassengers()
    }

    fun getPassengersForStation(stationUUID: String): List<Passenger> {
        val station = appDatabase.stationDao().getStationByUUID(stationUUID)
//        return station?.let { appDatabase.stationDao().getPassengersForStation(it.id) } ?: emptyList()
        return emptyList()
    }

    fun addPassengerForStation(stationUUID: String, passengerUUID: String, passengerName: String, passengerPhoneNumber: String) {
//        val station = appDatabase.stationDao().getStationByUUID(stationUUID)
//        if (station != null) {
//            val passenger = Passenger(uuid = passengerUUID, name = passengerName, phoneNumber = passengerPhoneNumber)
//            val passengerId = appDatabase.passengerDao().insertPassenger(passenger)
//            appDatabase.stationDao().insertStationPassengerCrossRef(StationPassengerCrossRef(station.id, passenger.id))
//        }
    }
}
