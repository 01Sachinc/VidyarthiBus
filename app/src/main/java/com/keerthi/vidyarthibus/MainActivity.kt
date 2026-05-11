package com.keerthi.vidyarthibus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.keerthi.vidyarthibus.navigation.NavGraph
import com.keerthi.vidyarthibus.ui.theme.VidyarthiBusTheme
import com.keerthi.vidyarthibus.utils.DataSeeder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var dataSeeder: DataSeeder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Seeding data to ensure real-time data is available
        dataSeeder.seedUsers()
        dataSeeder.seedBuses()

        setContent {
            VidyarthiBusTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController()
                    NavGraph(navController = navController)
                }
            }
        }
    }
}
