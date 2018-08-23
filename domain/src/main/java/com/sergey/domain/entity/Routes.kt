package com.sergey.domain.entity

data class Routes(
        val routes: List<Route>
)

data class Route(
        val legs: List<Leg>,
        val overview_polyline: OverviewPolyline
)

data class Leg(
        val distance: Distance,
        val duration: Duration,
        val end_address: String,
        val end_location: EndLocation,
        val start_address: String,
        val start_location: StartLocation,
        val steps: List<Step>,
        val traffic_speed_entry: List<Any>,
        val via_waypoint: List<Any>
)

data class Distance(
        val text: String,
        val value: Int
)

data class EndLocation(
        val lat: Double,
        val lng: Double
)

data class Duration(
        val text: String,
        val value: Int
)

data class StartLocation(
        val lat: Double,
        val lng: Double
)

data class Step(
        val distance: Distance,
        val duration: Duration,
        val end_location: EndLocation,
        val html_instructions: String,
        val polyline: Polyline,
        val start_location: StartLocation,
        val travel_mode: String,
        val maneuver: String
)

data class Polyline(
        val points: String
)

data class OverviewPolyline(
        val points: String
)