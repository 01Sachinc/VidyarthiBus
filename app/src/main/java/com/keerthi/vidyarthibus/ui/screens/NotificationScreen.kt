package com.keerthi.vidyarthibus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.keerthi.vidyarthibus.ui.components.AppToolbar
import com.keerthi.vidyarthibus.utils.Resource
import com.keerthi.vidyarthibus.viewmodel.NotificationViewModel
import java.util.*

@Composable
fun NotificationScreen(navController: NavController, viewModel: NotificationViewModel = hiltViewModel()) {
    val notificationState by viewModel.notifications.collectAsState()

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Notifications",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (val state = notificationState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(androidx.compose.ui.Alignment.Center))
                }
                is Resource.Success -> {
                    val notifications = state.data ?: emptyList()
                    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
                        items(notifications) { notification ->
                            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(text = notification.title, style = MaterialTheme.typography.titleMedium)
                                    Text(text = notification.message, style = MaterialTheme.typography.bodyMedium)
                                    Text(text = Date(notification.timestamp).toString(), style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    Text(text = state.message ?: "Error", modifier = Modifier.align(androidx.compose.ui.Alignment.Center))
                }
            }
        }
    }
}
