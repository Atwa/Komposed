package io.github.atwa.komposed.sample.core.navigation

import androidx.navigation.NavController
import io.github.atwa.komposed.Navigator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigatorImpl @Inject constructor() : Navigator {

    private var navController: NavController? = null

    fun bind(controller: NavController) {
        navController = controller
    }

    override fun <T : Any> navigate(route: T) = navController?.navigate(route) ?: Unit

    override fun navigateUp() {
        navController?.navigateUp()
    }

    override fun popBackStack() {
        navController?.popBackStack()
    }

    override fun <T : Any> popBackStackTo(route: T, inclusive: Boolean, saveState: Boolean) {
        navController?.popBackStack(route, inclusive, saveState)
    }
}