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
    val busImageUrl: String = "https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?q=80&w=2069&auto=format&fit=crop",
    val busType: String = "Express", // AC, Non-AC, Electric
    val stops: List<String> = emptyList(),
    val isPremium: Boolean = false,
    val pricePerMonth: Double = 0.0,
    val updatedAt: Long = System.currentTimeMillis(),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
