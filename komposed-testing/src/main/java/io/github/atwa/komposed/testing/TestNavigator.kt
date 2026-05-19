package io.github.atwa.komposed.testing

import io.github.atwa.komposed.Navigator

class TestNavigator : Navigator {

    val navigations = mutableListOf<Any>()
    var navigatedUpCount = 0
        private set
    var poppedBackStackCount = 0
        private set
    val poppedBackStackToRoutes = mutableListOf<Triple<Any, Boolean, Boolean>>()

    override fun <T : Any> navigate(route: T) {
        navigations.add(route)
    }

    override fun navigateUp() {
        navigatedUpCount++
    }

    override fun popBackStack() {
        poppedBackStackCount++
    }

    override fun <T : Any> popBackStackTo(route: T, inclusive: Boolean, saveState: Boolean) {
        poppedBackStackToRoutes.add(Triple(route, inclusive, saveState))
    }
}
