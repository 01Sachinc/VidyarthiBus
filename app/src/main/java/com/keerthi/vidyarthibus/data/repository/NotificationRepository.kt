package com.keerthi.vidyarthibus.data.repository

import com.keerthi.vidyarthibus.data.model.NotificationModel
import com.keerthi.vidyarthibus.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor() {
    // In a real app, this would fetch from Firebase or Local DB
    fun getNotifications(): Flow<Resource<List<NotificationModel>>> = flow {
        emit(Resource.Loading())
        val dummyList = listOf(
            NotificationModel("1", "Bus Delay", "Route 101 is delayed by 10 mins", System.currentTimeMillis()),
            NotificationModel("2", "Bus Full", "Route 102 is already full", System.currentTimeMillis() - 3600000)
        )
        emit(Resource.Success(dummyList))
    }
}
