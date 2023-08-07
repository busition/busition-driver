package io.busition.driver.roomDB

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "station")
data class Station(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val uuid: String,
)

@Entity(tableName = "passenger")
data class Passenger(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val uuid: String,
    val name: String,
    val phoneNumber: String,
)

@Entity(
    tableName = "station_passenger",
    primaryKeys = ["stationId", "passengerId"],
    foreignKeys = [
        ForeignKey(
            entity = Station::class,
            parentColumns = ["id"],
            childColumns = ["stationId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Passenger::class,
            parentColumns = ["id"],
            childColumns = ["passengerId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class StationPassengerCrossRef(
    val stationId: Int,
    val passengerId: Int
)