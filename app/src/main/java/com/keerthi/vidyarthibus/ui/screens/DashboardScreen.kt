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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
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
    
    LaunchedEffect(Unit) {
        while(true) {
            delay(4000)
            val nextPage = (pagerState.currentPage + 1) % 3
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.shadow(8.dp),
                color = MaterialTheme.colorScheme.primary
            ) {
                Column(
                    modifier = Modifier
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primary.copy(alpha = 0.8f))
                            )
                        )
                        .statusBarsPadding()
                        .padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Vidyarthi-Bus",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                )
                            )
                            Text(
                                text = if (currentUser != null) "Ready for class, ${currentUser.email?.split("@")?.get(0)}?" else "Browsing as Guest",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.7f))
                            )
                        }
                        Row {
                            IconButton(onClick = { navController.navigate(Routes.Notifications.route) }) {
                                Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White)
                            }
                            IconButton(onClick = {
                                if (currentUser != null) navController.navigate(Routes.Profile.route)
                                else navController.navigate(Routes.Login.route)
                            }) {
                                Icon(Icons.Default.AccountCircle, contentDescription = null, tint = Color.White)
                            }
                        }
                    }

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.onSearchQueryChanged(it) },
                        placeholder = { Text("Search city, route, or bus...", color = Color.White.copy(alpha = 0.5f), fontSize = 14.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(52.dp),
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White.copy(alpha = 0.7f)) },
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
                    
                    if (suggestions.isNotEmpty() && searchQuery.isEmpty()) {
                        LazyRow(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(suggestions) { city ->
                                SuggestionChip(city) { viewModel.onSearchQueryChanged(city) }
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
                .background(Color(0xFFF8F9FA)),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                        .clip(RoundedCornerShape(20.dp))
                ) { page ->
                    PremiumBanner(page, navController)
                }
                
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(3) { i ->
                        Box(
                            modifier = Modifier
                                .padding(3.dp)
                                .clip(CircleShape)
                                .background(if (pagerState.currentPage == i) MaterialTheme.colorScheme.primary else Color.LightGray)
                                .size(6.dp)
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Student Essentials",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 12.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ServiceItem("Live Map", Icons.Default.Map, Color(0xFFE3F2FD)) { navController.navigate(Routes.MapView.route) }
                    ServiceItem("My Pass", Icons.Default.ConfirmationNumber, Color(0xFFF1F8E9)) { navController.navigate(Routes.BusPass.route) }
                    ServiceItem("Premium", Icons.Default.Star, Color(0xFFFFF3E0)) { navController.navigate(Routes.Premium.route) }
                    ServiceItem("Helpline", Icons.Default.SupportAgent, Color(0xFFF3E5F5)) { navController.navigate(Routes.Notifications.route) }
                }
            }

            item {
                Text(
                    text = "Explore Services",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 12.dp)
                )
                
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    val rows = listOf(
                        listOf(
                            Triple("Safety", Icons.Default.Security, Color(0xFFFFEBEE)),
                            Triple("Wallet", Icons.Default.AccountBalanceWallet, Color(0xFFE3F2FD)),
                            Triple("History", Icons.Default.History, Color(0xFFF1F8E9)),
                            Triple("Rewards", Icons.Default.CardGiftcard, Color(0xFFFFF3E0))
                        ),
                        listOf(
                            Triple("News", Icons.Default.Newspaper, Color(0xFFF3E5F5)),
                            Triple("Forum", Icons.Default.Forum, Color(0xFFE0F7FA)),
                            Triple("Lost", Icons.Default.Search, Color(0xFFFBE9E7)),
                            Triple("More", Icons.Default.Apps, Color(0xFFECEFF1))
                        )
                    )
                    
                    rows.forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            row.forEach { (title, icon, color) ->
                                ServiceItemSmall(title, icon, color) {
                                    navController.navigate(Routes.FeatureDetail.createRoute(title.lowercase()))
                                }
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = if (searchQuery.isNotEmpty()) "Search Results" else "Available Routes",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 8.dp)
                )
            }

            when (val state = busesState) {
                is Resource.Loading -> {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().height(300.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(strokeWidth = 3.dp)
                        }
                    }
                }
                is Resource.Success -> {
                    val buses = state.data ?: emptyList()
                    if (buses.isEmpty()) {
                        item { EmptySearchState() }
                    } else {
                        items(buses, key = { it.routeId }) { bus ->
                            AnimatedVisibility(
                                visible = true,
                                enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn()
                            ) {
                                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                                    BusCard(bus = bus, onClick = {
                                        navController.navigate(Routes.BusDetails.createRoute(bus.routeId))
                                    })
                                }
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    item { ErrorState(state.message) }
                }
            }
        }
    }
}

@Composable
fun SuggestionChip(text: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        color = Color.White.copy(alpha = 0.15f),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
fun PremiumBanner(page: Int, navController: NavController) {
    val banners = listOf(
        Triple("Annual Bus Pass", "Get 30% off on your college commute", "https://images.unsplash.com/photo-1544620347-c4fd4a3d5957"),
        Triple("Live Real-time Tracking", "Know exactly when your bus arrives", "https://images.unsplash.com/photo-1570125909232-eb263c188f7e"),
        Triple("Premium Experience", "Exclusive AC routes & reserved seats", "https://images.unsplash.com/photo-1544620347-c4fd4a3d5957")
    )
    
    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = banners[page].third,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)))))
        
        Column(
            modifier = Modifier.align(Alignment.BottomStart).padding(20.dp)
        ) {
            Text(banners[page].first, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(banners[page].second, color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { navController.navigate(Routes.Premium.route) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text("Explore Now", fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun ServiceItem(title: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Surface(
            modifier = Modifier.size(64.dp),
            color = color,
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f), modifier = Modifier.size(28.dp))
            }
        }
        Text(title, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium), modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
fun ServiceItemSmall(title: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(70.dp).clickable { onClick() }
    ) {
        Surface(
            modifier = Modifier.size(50.dp),
            color = color,
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
            }
        }
        Text(
            text = title, 
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), 
            modifier = Modifier.padding(top = 4.dp),
            maxLines = 1
        )
    }
}

@Composable
fun EmptySearchState() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.SearchOff, contentDescription = null, modifier = Modifier.size(80.dp), tint = Color.LightGray)
        Spacer(modifier = Modifier.height(16.dp))
        Text("No buses found", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
        Text("Try searching for a city or route ID", style = MaterialTheme.typography.bodySmall, color = Color.LightGray)
    }
}

@Composable
fun ErrorState(message: String?) {
    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
        Text(text = message ?: "Unexpected error", color = MaterialTheme.colorScheme.error)
    }
}
