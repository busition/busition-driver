package io.busition.driver.roomDB
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PassengerDao {
    @Insert
    fun insertPassenger(passenger: Passenger)

    @Query("SELECT * FROM passenger")
    fun getAllPassengers(): List<Passenger>


    @Query("SELECT * FROM passenger WHERE stationId = :stationId")
    fun getPassengersByStationId(stationId: String): List<Passenger>

}
