package com.keerthi.vidyarthibus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keerthi.vidyarthibus.data.model.NotificationModel
import com.keerthi.vidyarthibus.data.repository.NotificationRepository
import com.keerthi.vidyarthibus.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepository
) : ViewModel() {

    private val _notifications = MutableStateFlow<Resource<List<NotificationModel>>>(Resource.Loading())
    val notifications: StateFlow<Resource<List<NotificationModel>>> = _notifications

    init {
        getNotifications()
    }

    fun getNotifications() {
        viewModelScope.launch {
            repository.getNotifications().collectLatest {
                _notifications.value = it
            }
        }
    }
}
