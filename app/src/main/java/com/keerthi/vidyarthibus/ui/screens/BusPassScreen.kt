package com.keerthi.vidyarthibus.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.keerthi.vidyarthibus.ui.components.AppToolbar

@Composable
fun BusPassScreen(navController: NavController) {
    Scaffold(
        topBar = {
            AppToolbar(
                title = "Student Bus Pass",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Your Digital Pass",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            // 3D-like Pass Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .graphicsLayer {
                        rotationX = 5f
                        rotationY = -5f
                        cameraDistance = 12f * density
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF6200EE), Color(0xFFBB86FC))
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Vidyarthi-Bus", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("2024-25", color = Color.White)
                        }
                        
                        Spacer(modifier = Modifier.height(40.dp))
                        
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .background(Color.White, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.QrCode, contentDescription = null, modifier = Modifier.size(120.dp), tint = Color.Black)
                        }
                        
                        Spacer(modifier = Modifier.height(40.dp))
                        
                        Text("KEERTHANA G K", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                        Text("ID: VB-2024-008", color = Color.White.copy(alpha = 0.8f))
                        
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Route: Madavara Banglore Nice Rode",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        HorizontalDivider(color = Color.White.copy(alpha = 0.3f))
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("VALIDITY", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                                Text("1 YEAR", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("STATUS", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                                Text("ACTIVE", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = { /* Download Pass */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Download Offline Pass")
            }
        }
    }
}
