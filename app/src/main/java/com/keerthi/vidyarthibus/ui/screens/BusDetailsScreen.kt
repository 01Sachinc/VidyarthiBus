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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
    
    // Fallback to "Guest" if not logged in
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "Guest_${UUID.randomUUID().toString().take(8)}"

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
                            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                                Text(text = bus.busNumber, style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
                                Spacer(modifier = Modifier.width(8.dp))
                                Badge(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                                    Text(bus.routeId, color = MaterialTheme.colorScheme.onPrimaryContainer)
                                }
                            }
                            Text(text = bus.routeName, style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Info Row
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                InfoItem("Speed", "${bus.speed} km/h")
                                InfoItem("Driver", bus.driverName)
                                InfoItem("Rating", "⭐ ${bus.driverRating}")
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                        Text("Next Stop:", fontWeight = FontWeight.Bold)
                                        Text(bus.nextStop, color = MaterialTheme.colorScheme.primary)
                                    }
                                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                        Text("Estimated Arrival:")
                                        Text(bus.arrivalTime, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                            
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

@Composable
fun InfoItem(label: String, value: String) {
    Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text(text = value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
    }
}
