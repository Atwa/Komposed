package io.github.atwa.komposed.testing

import io.github.atwa.komposed.Effect
import io.github.atwa.komposed.NavigationEffect
import io.github.atwa.komposed.PureReducer
import io.github.atwa.komposed.ReduceType
import io.github.atwa.komposed.testing.stateDiff

data class PropertyChange(val name: String, val before: Any?, val after: Any?)

data class ReduceResult<S>(
    val previousState: S,
    val nextState: S,
    val effect: Effect<*>?,
)

fun <S, A : Any> PureReducer<S, A>.given(state: S, action: A): ReduceResult<S> {
    val result = invoke(state, action)
    return when (result) {
        is ReduceType.Reduce -> ReduceResult(state, result.state, null)
        is ReduceType.ReduceWithEffect -> ReduceResult(state, result.state, result.effect)
        is ReduceType.SideEffect -> ReduceResult(state, state, result.effect)
    }
}

fun <S> ReduceResult<S>.assertState(expected: S): ReduceResult<S> {
    if (nextState != expected) throw AssertionError(
        "Expected state:\n$expected\n\nActual state:\n$nextState"
    )
    return this
}

fun <S> ReduceResult<S>.assertNoStateChange(): ReduceResult<S> {
    if (nextState != previousState) throw AssertionError(
        "Expected no state change. Diff:\n${stateDiff()}"
    )
    return this
}

fun <S> ReduceResult<S>.assertNoEffect(): ReduceResult<S> {
    if (effect != null) throw AssertionError("Expected no effect but got: $effect")
    return this
}

inline fun <reified E : Effect<*>> ReduceResult<*>.assertEffect(
    noinline verify: (E) -> Unit = {},
): ReduceResult<*> {
    if (effect !is E) throw AssertionError(
        "Expected effect of type ${E::class.simpleName} but got: $effect"
    )
    verify(effect)
    return this
}

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

fun <S> ReduceResult<S>.stateDiff(): List<PropertyChange> =
    (previousState as Any).javaClass.declaredFields
        .filter { !it.isSynthetic }
        .mapNotNull { field ->
            field.isAccessible = true
            val before = field.get(previousState)
            val after = field.get(nextState)
            if (before != after) PropertyChange(field.name, before, after) else null
        }
