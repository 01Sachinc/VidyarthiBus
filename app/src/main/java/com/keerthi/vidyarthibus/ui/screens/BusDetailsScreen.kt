package com.keerthi.vidyarthibus.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.keerthi.vidyarthibus.ui.components.AppToolbar
import com.keerthi.vidyarthibus.ui.components.CrowdMeter
import com.keerthi.vidyarthibus.utils.Constants
import com.keerthi.vidyarthibus.utils.Resource
import com.keerthi.vidyarthibus.viewmodel.BusViewModel
import java.util.*

@Composable
fun BusDetailsScreen(navController: NavController, routeId: String, viewModel: BusViewModel = hiltViewModel()) {
    val busState by viewModel.selectedBus.collectAsState()
    val reportState by viewModel.reportState.collectAsState()
    val context = LocalContext.current
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    LaunchedEffect(routeId) {
        viewModel.getBusById(routeId)
    }

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Bus Details",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (val state = busState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(androidx.compose.ui.Alignment.Center))
                }
                is Resource.Success -> {
                    val bus = state.data
                    if (bus != null) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Bus Number: ${bus.busNumber}", style = MaterialTheme.typography.headlineSmall)
                            Text(text = "Route: ${bus.routeId}", style = MaterialTheme.typography.titleMedium)
                            Text(text = "Last Updated: ${Date(bus.updatedAt)}", style = MaterialTheme.typography.bodySmall)
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            CrowdMeter(percentage = bus.crowdPercentage)
                            
                            Spacer(modifier = Modifier.height(32.dp))
                            Text(text = "Report Current Crowd Status:", style = MaterialTheme.typography.titleMedium)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                Button(onClick = { viewModel.reportCrowd(routeId, userId, Constants.CROWD_EMPTY) }) {
                                    Text("Empty")
                                }
                                Button(onClick = { viewModel.reportCrowd(routeId, userId, Constants.CROWD_SEATS_AVAILABLE) }) {
                                    Text("Seated")
                                }
                                Button(onClick = { viewModel.reportCrowd(routeId, userId, Constants.CROWD_FULL) }) {
                                    Text("Full")
                                }
                            }

                            Spacer(modifier = Modifier.height(32.dp))
                            Text(text = "Shared Auto Contacts:", style = MaterialTheme.typography.titleMedium)
                            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                                Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Driver: Ramesh")
                                    TextButton(onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:9876543210"))
                                        context.startActivity(intent)
                                    }) {
                                        Text("Call")
                                    }
                                }
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    Text(text = state.message ?: "Error")
                }
                null -> {}
            }
        }
    }
}
