package io.github.atwa.komposed.sample.core.navigation

import androidx.navigation.NavController
import androidx.navigation.navOptions
import io.github.atwa.komposed.navigation.NavOptions
import io.github.atwa.komposed.navigation.Navigator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigatorImpl @Inject constructor() : Navigator {

    private var navController: NavController? = null

    fun bind(controller: NavController) {
        navController = controller
    }

    override fun <T : Any> navigate(route: T) {
        navController?.navigate(route)
    }

    override fun <T : Any> navigate(route: T, navOptions: NavOptions) {
        val androidOptions = navOptions {
            launchSingleTop = navOptions.launchSingleTop
            restoreState = navOptions.restoreState
            navOptions.popUpTo?.let { popUpTo ->
                popUpTo(popUpTo.route) {
                    inclusive = popUpTo.inclusive
                    saveState = popUpTo.saveState
                }
            }
        }
        navController?.navigate(route, androidOptions)
    }

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
