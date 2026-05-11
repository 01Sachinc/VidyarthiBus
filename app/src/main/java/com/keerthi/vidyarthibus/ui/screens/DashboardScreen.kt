package com.keerthi.vidyarthibus.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.keerthi.vidyarthibus.navigation.Routes
import com.keerthi.vidyarthibus.ui.components.BusCard
import com.keerthi.vidyarthibus.utils.Resource
import com.keerthi.vidyarthibus.viewmodel.BusViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController, viewModel: BusViewModel = hiltViewModel()) {
    val busesState by viewModel.buses.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val suggestions by viewModel.locationSuggestions.collectAsState()
    val currentUser = remember { FirebaseAuth.getInstance().currentUser }

    val pagerState = rememberPagerState { 3 }
    
    // Auto-scroll pager
    LaunchedEffect(Unit) {
        while(true) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % 3
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.primary,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
                            )
                        )
                        .statusBarsPadding()
                        .padding(bottom = 16.dp)
                ) {
                    // Toolbar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Vidyarthi-Bus",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = if (currentUser != null) "Hello, ${currentUser.email?.split("@")?.get(0)}" else "Hello, Guest",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.8f))
                            )
                        }
                        Row {
                            IconButton(onClick = { navController.navigate(Routes.Notifications.route) }) {
                                Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White)
                            }
                            IconButton(onClick = {
                                if (currentUser != null) {
                                    navController.navigate(Routes.Profile.route)
                                } else {
                                    navController.navigate(Routes.Login.route)
                                }
                            }) {
                                Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.White)
                            }
                        }
                    }

                    // Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.onSearchQueryChanged(it) },
                        placeholder = { Text("Search route, city, state...", color = Color.White.copy(alpha = 0.6f)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    
                    // Location Suggestions
                    if (suggestions.isNotEmpty() && searchQuery.isEmpty()) {
                        LazyRow(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(suggestions) { city ->
                                AssistChip(
                                    onClick = { viewModel.onSearchQueryChanged(city) },
                                    label = { Text(city, color = Color.White) },
                                    colors = AssistChipDefaults.assistChipColors(containerColor = Color.White.copy(alpha = 0.2f)),
                                    border = null
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Carousel Item
            item {
                Column(modifier = Modifier.padding(vertical = 16.dp)) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) { page ->
                        CarouselImageItem(page, navController)
                    }
                    
                    Row(
                        Modifier
                            .height(20.dp)
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(3) { iteration ->
                            val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .size(8.dp)
                            )
                        }
                    }
                }
            }

            // Quick Actions Section
            item {
                Text(
                    text = "Quick Services",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    QuickActionItem("Live Bus", Icons.Default.DirectionsBus, Color(0xFFE3F2FD)) {
                        // For demo, navigate to first available bus tracking
                        if (busesState is Resource.Success) {
                            val bus = (busesState as Resource.Success).data?.firstOrNull()
                            if (bus != null) {
                                navController.navigate(Routes.Tracking.createRoute(bus.routeId))
                            }
                        }
                    }
                    QuickActionItem("Routes", Icons.Default.Map, Color(0xFFF1F8E9)) {
                        // Open map view
                    }
                    QuickActionItem("Pass", Icons.Default.ConfirmationNumber, Color(0xFFFFF3E0)) {
                        navController.navigate(Routes.BusPass.route)
                    }
                    QuickActionItem("More", Icons.Default.MoreHoriz, Color(0xFFF3E5F5)) { 
                        navController.navigate(Routes.Notifications.route)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Bus List Section
            item {
                Text(
                    text = if (searchQuery.isNotEmpty()) "Search Results" else "Available Buses",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            when (val state = busesState) {
                is Resource.Loading -> {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
                is Resource.Success -> {
                    val buses = state.data ?: emptyList()
                    if (buses.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.SearchOff, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                                Text("No buses found", style = MaterialTheme.typography.titleMedium)
                            }
                        }
                    } else {
                        items(buses, key = { it.routeId }) { bus ->
                            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                                BusCard(bus = bus, onClick = {
                                    navController.navigate(Routes.BusDetails.createRoute(bus.routeId))
                                })
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    item {
                        Text(
                            text = state.message ?: "Error loading buses",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CarouselImageItem(page: Int, navController: NavController) {
    val (title, sub, color) = when(page) {
        0 -> Triple("Student Bus Pass", "Apply for academic year 2024-25", Color(0xFF6200EE))
        1 -> Triple("Real-time Tracking", "Know exactly where your bus is", Color(0xFF03DAC6))
        else -> Triple("Safe Travel", "Crowd monitoring for safety", Color(0xFFFF0266))
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .clickable { 
                when(page) {
                    0 -> navController.navigate(Routes.BusPass.route)
                    1 -> {
                        // For demo, go to tracking for route 101
                        navController.navigate(Routes.Tracking.createRoute("101"))
                    }
                    else -> navController.navigate(Routes.Notifications.route)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(sub, color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { /* Action */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = color),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text("View Details", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
fun QuickActionItem(title: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = title, tint = Color.Black.copy(alpha = 0.7f))
        }
        Text(title, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp))
    }
}
