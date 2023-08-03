package io.busition.driver.roomDB

import androidx.room.Entity
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
    val uuid: String
)