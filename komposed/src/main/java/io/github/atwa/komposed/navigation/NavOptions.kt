package io.github.atwa.komposed.navigation

/**
 * Navigation options mirroring [androidx.navigation.NavOptions] but decoupled from the Android
 * framework, so reducers and tests never depend on Android types.
 *
 * Pass to [Navigator.navigate] to control back-stack behaviour:
 * ```kotlin
 * NavigationEffect {
 *     navigate(
 *         HomeRoute,
 *         NavOptions(
 *             launchSingleTop = true,
 *             popUpTo = PopUpTo(LoginRoute, inclusive = true),
 *         )
 *     )
 * }
 * ```
 *
 * @property launchSingleTop If `true`, reuses an existing instance of the destination instead of
 *   stacking a new one.
 * @property restoreState If `true`, restores the saved state of the destination if it was
 *   previously popped with `saveState = true`.
 * @property popUpTo Pops entries off the back stack up to (and optionally including) [PopUpTo.route]
 *   before navigating to the new destination.
 */
data class NavOptions(
    val launchSingleTop: Boolean = false,
    val restoreState: Boolean = false,
    val popUpTo: PopUpTo? = null,
)

/**
 * Describes the destination to pop up to before navigating, and how to do it.
 *
 * @property route The destination route to pop up to.
 * @property inclusive If `true`, the [route] destination itself is also removed from the stack.
 * @property saveState If `true`, the state of popped destinations is saved for later restoration.
 */
data class PopUpTo(
    val route: Any,
    val inclusive: Boolean = false,
    val saveState: Boolean = false,
)
