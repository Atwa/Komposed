package io.github.atwa.komposed.effect

import io.github.atwa.komposed.navigation.Navigator

/**
 * Marker for a side effect produced by a reducer.
 *
 * Declare feature-specific effects as sealed interfaces extending [Effect]:
 * ```kotlin
 * sealed interface DeliveryEffect : Effect {
 *     data object FetchAddresses : DeliveryEffect
 * }
 * ```
 *
 * The store routes each [Effect] to the appropriate handler based on its runtime type:
 * - [NavigationEffect] — re-dispatched through the middleware chain for [io.github.atwa.komposed.middleware.navigationMiddleware]
 * - All other subtypes — routed to a registered [EffectHandler] via the effect handler registry
 */
interface Effect

/** A navigation side effect expressed as a [Navigator] lambda.
 *
 * The store does not execute this effect directly. Instead it re-dispatches the instance back
 * through the middleware chain, where [io.github.atwa.komposed.middleware.navigationMiddleware]
 * intercepts it and invokes [block] with the bound [Navigator]. Reducers never receive a
 * [NavigationEffect]. */
data class NavigationEffect(val block: Navigator.() -> Unit) : Effect
