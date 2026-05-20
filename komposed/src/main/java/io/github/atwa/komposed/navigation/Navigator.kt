package io.github.atwa.komposed.navigation

/**
 * Navigation DSL exposed to reducers via [io.github.atwa.komposed.effect.NavigationEffect] — hides
 * [androidx.navigation.NavController] completely.
 *
 * Reducers never hold a reference to this interface. They express navigation intent by returning a
 * [io.github.atwa.komposed.effect.NavigationEffect] whose lambda receives a [Navigator] at execution
 * time, keeping reducers pure and fully testable with [io.github.atwa.komposed.testing.TestNavigator].
 */
interface Navigator {

    /** Navigates to [route] using type-safe Navigation Compose routes. */
    fun <T : Any> navigate(route: T)

    /**
     * Navigates to [route] with the given [navOptions].
     *
     * Use this overload to control back-stack behaviour — single-top launches, pop-up-to, or
     * state restoration — without referencing Android framework types in reducers or tests.
     */
    fun <T : Any> navigate(route: T, navOptions: NavOptions)

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
