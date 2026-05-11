package com.keerthi.vidyarthibus.data.model

data class Bus(
    val routeId: String = "",
    val busNumber: String = "",
    val routeName: String = "",
    val city: String = "",
    val state: String = "",
    val country: String = "India",
    val crowdLevel: String = "EMPTY", // EMPTY, SEATS_AVAILABLE, FULL
    val crowdPercentage: Int = 0,
    val speed: Int = 0,
    val driverName: String = "Unknown",
    val driverRating: Float = 4.5f,
    val nextStop: String = "Calculating...",
    val arrivalTime: String = "--:--",
    val updatedAt: Long = System.currentTimeMillis(),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
