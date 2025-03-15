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

    fun getPassengerList(stationId:String): List<Passenger>{
        return appDatabase.passengerDao().getPassengersByStationId(stationId)
    }

    fun insertPassenger(passenger: Passenger){
        appDatabase.passengerDao().insertPassenger(passenger)
    }


}
