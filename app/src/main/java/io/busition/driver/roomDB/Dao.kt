package io.busition.driver.roomDB
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface StationDao {

//    @Insert
//    fun insertStationPassengerCrossRef(ref: StationPassengerCrossRef)
    @Insert
    fun insertStation(station: Station)

    @Query("SELECT * FROM station")
    fun getAllStations(): List<Station>

    @Query("SELECT * FROM station WHERE uuid = :uuid")
    fun getStationByUUID(uuid: String): Station?

//    @Transaction
//    @Query("SELECT * FROM passenger WHERE id IN (SELECT passengerId FROM station_passenger WHERE stationId = :stationId)")
//    fun getPassengersForStation(stationId: Int): List<Passenger>
}

@Dao
interface PassengerDao {
    @Insert
    fun insertPassenger(passenger: Passenger)

    @Query("SELECT * FROM passenger")
    fun getAllPassengers(): List<Passenger>
}
