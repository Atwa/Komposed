package io.github.atwa.komposed.testing

import io.github.atwa.komposed.navigation.NavOptions
import io.github.atwa.komposed.navigation.Navigator

/**
 * In-memory [Navigator] implementation for use in reducer unit tests.
 *
 * Pass it to [ReduceResult.assertNavigationEffect] to verify that a [NavigationEffect] calls
 * the correct navigation methods. All interactions are recorded in the properties below so
 * assertions can be made after the effect executes.
 *
 * ```kotlin
 * reducer.given(state, PlaceOrderAction.OrderPlaced(orderId = "42"))
 *     .assertNavigationEffect { nav ->
 *         assert(nav.navigations.last() == OrderDetailsRoute(orderId = "42"))
 *     }
 * ```
 */
class TestNavigator : Navigator {

    /** Every route argument passed to [navigate] (with or without options), in call order. */
    val navigations = mutableListOf<Any>()

    /**
     * Every route and [NavOptions] pair passed to [navigate], in call order.
     * Use this for assertions when navigation options matter.
     */
    val navigationsWithOptions = mutableListOf<Pair<Any, NavOptions>>()

    /** Number of times [navigateUp] was called. */
    var navigatedUpCount = 0
        private set

    /** Number of times [popBackStack] was called. */
    var poppedBackStackCount = 0
        private set

    /** Arguments of each [popBackStackTo] call as `Triple(route, inclusive, saveState)`. */
    val poppedBackStackToRoutes = mutableListOf<Triple<Any, Boolean, Boolean>>()

    override fun <T : Any> navigate(route: T) {
        navigations.add(route)
    }

    override fun <T : Any> navigate(route: T, navOptions: NavOptions) {
        navigations.add(route)
        navigationsWithOptions.add(Pair(route, navOptions))
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
