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
            Bus("101", "KA-01-F-1234", "College Main Route", "Bangalore", "Karnataka", "India", "EMPTY", 15, 40, "Ramesh Kumar", 4.8f, "Majestic", "10:30 AM"),
            Bus("102", "KA-02-G-5678", "North Campus Express", "Bangalore", "Karnataka", "India", "SEATS_AVAILABLE", 45, 35, "Suresh P", 4.2f, "Hebbal", "11:15 AM"),
            Bus("103", "KA-03-H-9012", "Rural Connect", "Mysore", "Karnataka", "India", "FULL", 90, 45, "Mahesh", 4.5f, "City Bus Stand", "12:00 PM"),
            Bus("104", "MH-01-A-1111", "City Center Link", "Mumbai", "Maharashtra", "India", "EMPTY", 10, 30, "Abdul", 4.7f, "Andheri", "09:45 AM"),
            Bus("105", "DL-01-B-2222", "Metro Station Shuttle", "Delhi", "Delhi", "India", "SEATS_AVAILABLE", 60, 25, "Rajesh", 4.0f, "Hauz Khas", "08:30 AM"),
            Bus("106", "KA-05-M-4444", "South Link", "Mangalore", "Karnataka", "India", "EMPTY", 5, 50, "Somu", 4.9f, "Pumpwell", "02:00 PM")
        )

        CoroutineScope(Dispatchers.IO).launch {
            buses.forEach { bus ->
                db.getReference(Constants.ROUTES_COLLECTION).child(bus.routeId).setValue(bus)
            }
        }
    }
}
