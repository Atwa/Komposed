package io.github.atwa.komposed.testing

import io.github.atwa.komposed.effect.Effect
import io.github.atwa.komposed.effect.NavigationEffect
import io.github.atwa.komposed.reducer.Reducer
import io.github.atwa.komposed.reducer.ReduceType
import io.github.atwa.komposed.testing.stateDiff

/** A field-level difference between [previousState] and [nextState], used by [ReduceResult.stateDiff]. */
data class PropertyChange(val name: String, val before: Any?, val after: Any?)

/**
 * The captured output of a single reducer invocation, used for unit-level assertions.
 *
 * Obtain via [Reducer.given]:
 * ```kotlin
 * deliveryReducer.given(DeliveryState(), DeliveryAction.FetchDeliveryAddresses)
 *     .assertState(DeliveryState(isLoading = true))
 *     .assertEffect<DeliveryEffect.FetchAddresses>()
 * ```
 *
 * @property previousState the state passed in to the reducer.
 * @property nextState the state returned by the reducer.
 * @property effect the [Effect] emitted by the reducer, or `null` if the reducer returned [ReduceType.Reduce].
 */
data class ReduceResult<S>(
    val previousState: S,
    val nextState: S,
    val effect: Effect?,
)

/** Invokes this reducer with [state] and [action] and wraps the result in a [ReduceResult]. */
fun <S, A : Any> Reducer<S, A>.given(state: S, action: A): ReduceResult<S> {
    val result = invoke(state, action)
    return when (result) {
        is ReduceType.Reduce -> ReduceResult(state, result.state, null)
        is ReduceType.ReduceWithEffect -> ReduceResult(state, result.state, result.effect)
        is ReduceType.SideEffect -> ReduceResult(state, state, result.effect)
    }
}

/** Asserts that [nextState] equals [expected]. Returns `this` for chaining. */
fun <S> ReduceResult<S>.assertState(expected: S): ReduceResult<S> {
    if (nextState != expected) throw AssertionError(
        "Expected state:\n$expected\n\nActual state:\n$nextState"
    )
    return this
}

/** Asserts that [nextState] is unchanged from [previousState]. Returns `this` for chaining. */
fun <S> ReduceResult<S>.assertNoStateChange(): ReduceResult<S> {
    if (nextState != previousState) throw AssertionError(
        "Expected no state change. Diff:\n${stateDiff()}"
    )
    return this
}

/** Asserts that no [Effect] was emitted. Returns `this` for chaining. */
fun <S> ReduceResult<S>.assertNoEffect(): ReduceResult<S> {
    if (effect != null) throw AssertionError("Expected no effect but got: $effect")
    return this
}

/**
 * Asserts that the emitted [Effect] is of type [E] and optionally inspects it with [verify].
 * Returns `this` for chaining.
 */
inline fun <reified E : Effect> ReduceResult<*>.assertEffect(
    noinline verify: (E) -> Unit = {},
): ReduceResult<*> {
    if (effect !is E) throw AssertionError(
        "Expected effect of type ${E::class.simpleName} but got: $effect"
    )
    verify(effect)
    return this
}

/**
 * Asserts that the emitted effect is a [NavigationEffect], executes it against a [TestNavigator],
 * and passes the spy to [verify] for inspection. Returns `this` for chaining.
 */
fun <S> ReduceResult<S>.assertNavigationEffect(
    verify: (TestNavigator) -> Unit,
): ReduceResult<S> {
    if (effect !is NavigationEffect) throw AssertionError(
        "Expected NavigationEffect but got: $effect"
    )
    val nav = TestNavigator()
    effect.block(nav)
    verify(nav)
    return this
}

/**
 * Returns a list of [PropertyChange] entries for every field that differs between
 * [previousState] and [nextState] — useful for debugging assertion failures.
 */
fun <S> ReduceResult<S>.stateDiff(): List<PropertyChange> =
    (previousState as Any).javaClass.declaredFields
        .filter { !it.isSynthetic }
        .mapNotNull { field ->
            field.isAccessible = true
            val before = field.get(previousState)
            val after = field.get(nextState)
            if (before != after) PropertyChange(field.name, before, after) else null
        }
