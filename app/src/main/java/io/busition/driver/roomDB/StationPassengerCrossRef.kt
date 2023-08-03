package io.busition.driver.roomDB

import androidx.room.Entity
import androidx.room.ForeignKey

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

