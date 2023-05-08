package com.micrantha.bluebell.ui.navi

class NavigationRoutes(
    val initialContext: RouteContext,
    private val routes: MappedRoutes
) : MappedRoutes by routes
