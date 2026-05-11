package com.keerthi.vidyarthibus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.keerthi.vidyarthibus.navigation.Routes
import com.keerthi.vidyarthibus.ui.components.AppToolbar
import com.keerthi.vidyarthibus.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Profile",
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
            if (user != null) {
                Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Email: ${user.email ?: "N/A"}", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "UID: ${user.uid}", style = MaterialTheme.typography.bodySmall)
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        auth.signOut()
                        navController.navigate(Routes.Login.route) {
                            popUpTo(Routes.Dashboard.route) { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Logout")
                }
            } else {
                Text(text = "You are currently browsing as a Guest", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { navController.navigate(Routes.Login.route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login / Sign Up")
                }
            }
        }
    }
}
