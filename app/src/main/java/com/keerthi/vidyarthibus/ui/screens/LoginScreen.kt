package com.keerthi.vidyarthibus.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.keerthi.vidyarthibus.navigation.Routes
import com.keerthi.vidyarthibus.ui.components.GradientButton
import com.keerthi.vidyarthibus.ui.components.LoadingDialog
import com.keerthi.vidyarthibus.utils.Resource
import com.keerthi.vidyarthibus.viewmodel.AuthViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        if (authState is Resource.Success) {
            navController.navigate(Routes.Dashboard.route) {
                popUpTo(Routes.Login.route) { inclusive = true }
            }
        } else if (authState is Resource.Error) {
            Toast.makeText(context, authState?.message, Toast.LENGTH_SHORT).show()
        }
    }

    if (authState is Resource.Loading) {
        LoadingDialog()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Welcome Back", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
        GradientButton(
            text = "Login",
            onClick = { viewModel.login(email, password) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = { navController.navigate(Routes.Register.route) }) {
            Text("Don't have an account? Register")
        }
    }
}
