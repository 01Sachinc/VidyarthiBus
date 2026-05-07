package com.keerthi.vidyarthibus.navigation

sealed class Routes(val route: String) {
    object Splash : Routes("splash")
    object Login : Routes("login")
    object Register : Routes("register")
    object Dashboard : Routes("dashboard")
    object RouteSelection : Routes("route_selection")
    object BusDetails : Routes("bus_details/{routeId}") {
        fun createRoute(routeId: String) = "bus_details/$routeId"
    }
    object Profile : Routes("profile")
    object Notifications : Routes("notifications")
}
