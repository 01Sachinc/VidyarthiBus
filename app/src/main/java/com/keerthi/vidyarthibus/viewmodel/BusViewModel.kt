package com.keerthi.vidyarthibus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keerthi.vidyarthibus.data.model.Bus
import com.keerthi.vidyarthibus.data.repository.BusRepository
import com.keerthi.vidyarthibus.domain.usecase.ReportCrowdUseCase
import com.keerthi.vidyarthibus.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusViewModel @Inject constructor(
    private val repository: BusRepository,
    private val reportCrowdUseCase: ReportCrowdUseCase
) : ViewModel() {

    private val _buses = MutableStateFlow<Resource<List<Bus>>>(Resource.Loading())
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val buses: StateFlow<Resource<List<Bus>>> = combine(_buses, _searchQuery) { resource, query ->
        when (resource) {
            is Resource.Success -> {
                val filteredList = resource.data?.filter { bus ->
                    bus.routeName.contains(query, ignoreCase = true) ||
                    bus.busNumber.contains(query, ignoreCase = true) ||
                    bus.city.contains(query, ignoreCase = true) ||
                    bus.state.contains(query, ignoreCase = true) ||
                    bus.country.contains(query, ignoreCase = true) ||
                    bus.routeId.contains(query, ignoreCase = true)
                }
                Resource.Success(filteredList ?: emptyList())
            }
            else -> resource
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Resource.Loading())

    val locationSuggestions: StateFlow<List<String>> = _buses.map { resource ->
        if (resource is Resource.Success) {
            val cities = resource.data?.map { it.city }?.distinct() ?: emptyList()
            val states = resource.data?.map { it.state }?.distinct() ?: emptyList()
            (cities + states).distinct().take(10)
        } else {
            emptyList()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
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
