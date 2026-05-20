package io.github.atwa.komposed.testing

import io.github.atwa.komposed.effect.Effect
import io.github.atwa.komposed.effect.EffectHandler

/**
 * Invokes this handler with [effect], collects every action passed to `dispatch`, and returns
 * them in call order.
 *
 * Replaces the capture-variable pattern in handler tests:
 * ```kotlin
 * // before
 * var result: BillAction? = null
 * handler.handle(effect) { result = it() }
 * assert(result == BillAction.BillSummaryLoaded(summary))
 *
 * // after
 * val result = handler.handle(effect)
 * assert(result.single() == BillAction.BillSummaryLoaded(summary))
 * ```
 *
 * Use [List.single] when exactly one action is expected, [List.isEmpty] for fire-and-forget
 * effects, or iterate for streaming effects.
 */
suspend fun <E : Effect, A : Any> EffectHandler<E, A>.handle(effect: E): List<A> {
    val dispatched = mutableListOf<A>()
    handle(effect) { dispatched.add(it()) }
    return dispatched
}
