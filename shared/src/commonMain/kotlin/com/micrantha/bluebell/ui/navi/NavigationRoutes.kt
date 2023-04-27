package com.micrantha.bluebell.ui.navi

class NavigationRoutes(
    val initialContext: RouteContext,
    private val routes: MappedRoutes
) : Map<Route, RouteRenderer> by routes
