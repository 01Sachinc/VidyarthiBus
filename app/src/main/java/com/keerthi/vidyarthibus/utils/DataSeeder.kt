package com.keerthi.vidyarthibus.utils

import com.google.firebase.database.FirebaseDatabase
import com.keerthi.vidyarthibus.data.model.Bus
import com.keerthi.vidyarthibus.data.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataSeeder @Inject constructor(
    private val authRepository: AuthRepository,
    private val db: FirebaseDatabase
) {
    fun seedUsers() {
        val users = listOf(
            Triple("Admin User", "adminuser@gmail.com", "admin@123"),
            Triple("Demo User", "demouser@gmail.com", "demo@123"),
            Triple("Test User", "testuser@gmail.com", "test@123")
        )

        CoroutineScope(Dispatchers.IO).launch {
            users.forEach { (name, email, pass) ->
                authRepository.register(name, email, pass, "101")
            }
        }
    }

    fun seedBuses() {
        val buses = listOf(
            Bus(
                routeId = "101", 
                busNumber = "KA-01-F-1234", 
                routeName = "Madavara to Majestic", 
                city = "Bangalore", 
                state = "Karnataka", 
                country = "India", 
                crowdLevel = "EMPTY", 
                crowdPercentage = 15, 
                speed = 40, 
                driverName = "Ramesh Kumar", 
                driverRating = 4.8f, 
                nextStop = "Nagasandra", 
                arrivalTime = "10:30 AM",
                busImageUrl = "https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?q=80&w=500",
                busType = "Electric AC",
                stops = listOf("Madavara", "Nagasandra", "Dasarahalli", "Jalahalli", "Peenya", "Yeshwanthpur", "Majestic"),
                isPremium = true,
                pricePerMonth = 499.0
            ),
            Bus(
                routeId = "102", 
                busNumber = "KA-02-G-5678", 
                routeName = "Hebbal Express", 
                city = "Bangalore", 
                state = "Karnataka", 
                country = "India", 
                crowdLevel = "SEATS_AVAILABLE", 
                crowdPercentage = 45, 
                speed = 35, 
                driverName = "Suresh P", 
                driverRating = 4.2f, 
                nextStop = "Manyata Tech Park", 
                arrivalTime = "11:15 AM",
                busImageUrl = "https://images.unsplash.com/photo-1570125909232-eb263c188f7e?q=80&w=500",
                busType = "Non-AC",
                stops = listOf("Hebbal", "Manyata Tech Park", "Nagawara", "Hennur Cross", "Kalyan Nagar"),
                isPremium = false
            ),
            Bus(
                routeId = "103", 
                busNumber = "KA-03-H-9012", 
                routeName = "Mysore Rural Connect", 
                city = "Mysore", 
                state = "Karnataka", 
                country = "India", 
                crowdLevel = "FULL", 
                crowdPercentage = 90, 
                speed = 45, 
                driverName = "Mahesh", 
                driverRating = 4.5f, 
                nextStop = "KRS Road", 
                arrivalTime = "12:00 PM",
                busImageUrl = "https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?q=80&w=500",
                busType = "Express",
                stops = listOf("City Bus Stand", "KRS Road", "Hootagalli", "Belavadi"),
                isPremium = true,
                pricePerMonth = 299.0
            ),
            Bus(
                routeId = "104", 
                busNumber = "MH-01-A-1111", 
                routeName = "Andheri West Link", 
                city = "Mumbai", 
                state = "Maharashtra", 
                country = "India", 
                crowdLevel = "EMPTY", 
                crowdPercentage = 10, 
                speed = 30, 
                driverName = "Abdul", 
                driverRating = 4.7f, 
                nextStop = "Juhu", 
                arrivalTime = "09:45 AM",
                busImageUrl = "https://images.unsplash.com/photo-1570125909232-eb263c188f7e?q=80&w=500",
                busType = "Double Decker AC",
                stops = listOf("Andheri", "Juhu", "Vile Parle", "Santacruz")
            )
        )

        CoroutineScope(Dispatchers.IO).launch {
            buses.forEach { bus ->
                db.getReference(Constants.ROUTES_COLLECTION).child(bus.routeId).setValue(bus)
            }
        }
    }
}
