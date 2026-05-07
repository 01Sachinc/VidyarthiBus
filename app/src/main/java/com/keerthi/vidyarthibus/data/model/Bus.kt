package com.keerthi.vidyarthibus.data.model

data class Bus(
    val routeId: String = "",
    val busNumber: String = "",
    val crowdLevel: String = "EMPTY", // EMPTY, SEATS_AVAILABLE, FULL
    val crowdPercentage: Int = 0,
    val updatedAt: Long = System.currentTimeMillis(),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
