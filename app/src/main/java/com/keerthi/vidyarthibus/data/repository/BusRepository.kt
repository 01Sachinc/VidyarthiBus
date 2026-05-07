package com.keerthi.vidyarthibus.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.keerthi.vidyarthibus.data.model.Bus
import com.keerthi.vidyarthibus.data.model.CrowdReport
import com.keerthi.vidyarthibus.utils.Constants
import com.keerthi.vidyarthibus.utils.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BusRepository @Inject constructor(
    private val db: FirebaseDatabase
) {
    fun getBuses(): Flow<Resource<List<Bus>>> = callbackFlow {
        trySend(Resource.Loading())
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val buses = snapshot.children.mapNotNull { it.getValue(Bus::class.java)?.copy(routeId = it.key ?: "") }
                trySend(Resource.Success(buses))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Resource.Error(error.message))
            }
        }
        db.getReference(Constants.ROUTES_COLLECTION).addValueEventListener(listener)
        awaitClose { db.getReference(Constants.ROUTES_COLLECTION).removeEventListener(listener) }
    }

    fun getBusById(routeId: String): Flow<Resource<Bus>> = callbackFlow {
        trySend(Resource.Loading())
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bus = snapshot.getValue(Bus::class.java)?.copy(routeId = snapshot.key ?: "")
                if (bus != null) trySend(Resource.Success(bus))
                else trySend(Resource.Error("Bus not found"))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Resource.Error(error.message))
            }
        }
        db.getReference(Constants.ROUTES_COLLECTION).child(routeId).addValueEventListener(listener)
        awaitClose { db.getReference(Constants.ROUTES_COLLECTION).child(routeId).removeEventListener(listener) }
    }

    suspend fun reportCrowd(routeId: String, userId: String, status: String): Resource<Unit> {
        return try {
            val report = CrowdReport(userId, status)
            db.getReference(Constants.REPORTS_COLLECTION).child(routeId).child(userId).setValue(report).await()
            
            // Update the bus crowd level based on logic (simplified here)
            // In a real app, this might be a cloud function or aggregation logic
            db.getReference(Constants.ROUTES_COLLECTION).child(routeId).child("crowdLevel").setValue(status).await()
            db.getReference(Constants.ROUTES_COLLECTION).child(routeId).child("updatedAt").setValue(System.currentTimeMillis()).await()
            
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to report crowd")
        }
    }
}
