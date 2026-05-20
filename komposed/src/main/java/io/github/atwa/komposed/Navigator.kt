package io.github.atwa.komposed

/**
 * Navigation DSL exposed to reducers via [NavigationEffect] — hides [androidx.navigation.NavController] completely.
 *
 * Reducers never hold a reference to this interface. They express navigation intent by returning a
 * [NavigationEffect] whose lambda receives a [Navigator] at execution time, keeping reducers pure
 * and fully testable with [io.github.atwa.komposed.testing.TestNavigator].
 */
interface Navigator {
    /** Navigates to [route] using type-safe Navigation Compose routes. */
    fun <T : Any> navigate(route: T)

    /** Navigates up the back stack, equivalent to the system Back action. */
    fun navigateUp()

    /** Pops the current destination off the back stack. */
    fun popBackStack()

    /**
     * Pops the back stack to [route].
     *
     * @param inclusive if `true`, [route] itself is also removed from the stack.
     * @param saveState if `true`, the popped entries' state is saved for restoration.
     */
    fun <T : Any> popBackStackTo(route: T, inclusive: Boolean = false, saveState: Boolean = false)
}
