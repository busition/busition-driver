package com.example.busition_driver.main_screen.placeholder

class ScheduleClass(
    routeId: String,
    departurePoint: String,
    destination: String,
    departureTime: String,
    shuttleDirection: String
) {
    private var routeId: String
    private var departurePoint: String
    private var destination: String
    private var departureTime: String
    private var shuttleDirection: String

    init {
        this.routeId = routeId
        this.departurePoint = departurePoint
        this.destination = destination
        this.departureTime = departureTime
        this.shuttleDirection = shuttleDirection
    }

    fun getRouteId(): String {
        return this.routeId
    }


    fun getDestination(): String {
        return this.departurePoint + " -> " + this.destination
    }

    fun getDepartureTime(): String {
        return this.departureTime
    }

    fun getShuttleDirection(): String {
        return this.shuttleDirection
    }
}