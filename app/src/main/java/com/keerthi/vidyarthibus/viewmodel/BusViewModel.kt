package com.keerthi.vidyarthibus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keerthi.vidyarthibus.data.model.Bus
import com.keerthi.vidyarthibus.data.repository.BusRepository
import com.keerthi.vidyarthibus.domain.usecase.ReportCrowdUseCase
import com.keerthi.vidyarthibus.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusViewModel @Inject constructor(
    private val repository: BusRepository,
    private val reportCrowdUseCase: ReportCrowdUseCase
) : ViewModel() {

    private val _buses = MutableStateFlow<Resource<List<Bus>>>(Resource.Loading())
    val buses: StateFlow<Resource<List<Bus>>> = _buses

    private val _selectedBus = MutableStateFlow<Resource<Bus>?>(null)
    val selectedBus: StateFlow<Resource<Bus>?> = _selectedBus

    private val _reportState = MutableStateFlow<Resource<Unit>?>(null)
    val reportState: StateFlow<Resource<Unit>?> = _reportState

    init {
        getBuses()
    }

    fun getBuses() {
        viewModelScope.launch {
            repository.getBuses().collectLatest {
                _buses.value = it
            }
        }
    }

    fun getBusById(routeId: String) {
        viewModelScope.launch {
            repository.getBusById(routeId).collectLatest {
                _selectedBus.value = it
            }
        }
    }

    fun reportCrowd(routeId: String, userId: String, status: String) {
        viewModelScope.launch {
            _reportState.value = Resource.Loading()
            _reportState.value = reportCrowdUseCase(routeId, userId, status)
        }
    }
}
