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
    object Tracking : Routes("tracking/{routeId}") {
        fun createRoute(routeId: String) = "tracking/$routeId"
    }
    object BusPass : Routes("bus_pass")
    object MapView : Routes("map_view")
    object Premium : Routes("premium")
    object FeatureDetail : Routes("feature_detail/{featureId}") {
        fun createRoute(featureId: String) = "feature_detail/$featureId"
    }
}
