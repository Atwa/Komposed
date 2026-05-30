package io.github.atwa.komposed.effect

/**
 * Handles a typed [Effect] value produced by a reducer, dispatching zero, one, or many actions
 * back into the store via [dispatch].
 *
 * Implement this interface directly on your handler class — no intermediate feature interface needed:
 *
 * ```kotlin
 * class DeliveryEffectHandlerImpl @Inject constructor(
 *     private val repository: DeliveryRepository,
 * ) : EffectHandler<DeliveryEffect, DeliveryAction> {
 *     override suspend fun handle(effect: DeliveryEffect, dispatch: suspend (suspend () -> DeliveryAction) -> Unit) {
 *         when (effect) {
 *             DeliveryEffect.FetchAddresses -> dispatch { repository.fetch().fold(...) }
 *             DeliveryEffect.LogView -> analytics.log("viewed") // no dispatch
 *             is DeliveryEffect.Watch -> repository.stream().collect { dispatch { DeliveryAction.Updated(it) } }
 *         }
 *     }
 * }
 * ```
 *
 * - Call [dispatch] with a lambda returning the action for a single-action result.
 * - Omit [dispatch] entirely for fire-and-forget effects such as analytics or cache writes.
 * - Call [dispatch] multiple times or collect a [kotlinx.coroutines.flow.Flow] for streaming results.
 *
 * Register via [io.github.atwa.komposed.reducer.EffectHandlerRegistry.register] inside the
 * [io.github.atwa.komposed.reducer.effectHandlers] DSL block.
 */
interface EffectHandler<E : Effect, A : Any> {
    suspend fun handle(effect: E, dispatch: suspend (suspend () -> A) -> Unit)
}
