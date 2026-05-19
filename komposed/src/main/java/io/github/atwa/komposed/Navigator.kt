package io.github.atwa.komposed

/** Navigation DSL exposed to reducers — hides [androidx.navigation.NavController] completely. */
interface Navigator {
    fun <T : Any> navigate(route: T)
    fun navigateUp()
    fun popBackStack()
    fun <T : Any> popBackStackTo(route: T, inclusive: Boolean = false, saveState: Boolean = false)
}