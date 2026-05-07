package com.keerthi.vidyarthibus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.keerthi.vidyarthibus.navigation.Routes
import com.keerthi.vidyarthibus.ui.components.AppToolbar
import com.keerthi.vidyarthibus.ui.components.BusCard
import com.keerthi.vidyarthibus.utils.Resource
import com.keerthi.vidyarthibus.viewmodel.BusViewModel

@Composable
fun DashboardScreen(navController: NavController, viewModel: BusViewModel = hiltViewModel()) {
    val busesState by viewModel.buses.collectAsState()

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Vidyarthi-Bus",
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.Notifications.route) }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                    IconButton(onClick = { navController.navigate(Routes.Profile.route) }) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (val state = busesState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(androidx.compose.ui.Alignment.Center))
                }
                is Resource.Success -> {
                    val buses = state.data ?: emptyList()
                    if (buses.isEmpty()) {
                        Text(
                            text = "No buses available",
                            modifier = Modifier.align(androidx.compose.ui.Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(buses) { bus ->
                                BusCard(bus = bus, onClick = {
                                    navController.navigate(Routes.BusDetails.createRoute(bus.routeId))
                                })
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    Text(
                        text = state.message ?: "Error loading buses",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(androidx.compose.ui.Alignment.Center)
                    )
                }
            }
        }
    }
}
