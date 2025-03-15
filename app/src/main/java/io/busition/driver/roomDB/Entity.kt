package io.busition.driver.roomDB

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "passenger")
data class Passenger(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val uuid: String,
    val name: String,
    val phoneNumber: String,
    val stationId: String,
)

