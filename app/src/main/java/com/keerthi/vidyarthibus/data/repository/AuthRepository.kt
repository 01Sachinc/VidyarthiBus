package com.keerthi.vidyarthibus.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.keerthi.vidyarthibus.data.model.User
import com.keerthi.vidyarthibus.utils.Constants
import com.keerthi.vidyarthibus.utils.Resource
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseDatabase
) {
    val currentUser: FirebaseUser? get() = auth.currentUser

    suspend fun login(email: String, pass: String): Resource<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, pass).await()
            result.user?.let {
                Resource.Success(it)
            } ?: Resource.Error("Login failed")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun register(name: String, email: String, pass: String, route: String): Resource<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, pass).await()
            result.user?.let { firebaseUser ->
                val user = User(firebaseUser.uid, name, email, route)
                db.getReference(Constants.USERS_COLLECTION).child(firebaseUser.uid).setValue(user).await()
                Resource.Success(firebaseUser)
            } ?: Resource.Error("Registration failed")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    fun logout() = auth.signOut()
}
