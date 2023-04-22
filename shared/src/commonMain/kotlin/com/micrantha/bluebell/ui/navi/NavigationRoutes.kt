package com.micrantha.bluebell.ui.navi

class NavigationRoutes(
    val defaultRoute: Route,
    private val routes: MappedRoutes
) : Map<Route, RouteRenderer> by routes
