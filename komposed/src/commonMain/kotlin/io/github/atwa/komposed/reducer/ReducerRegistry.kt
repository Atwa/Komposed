package io.github.atwa.komposed.reducer

import io.github.atwa.komposed.Lens
import io.github.atwa.komposed.effect.Effect
import io.github.atwa.komposed.effect.EffectHandler
import kotlin.reflect.KClass

/** Collects [Reducer]s keyed by their handled action type. */
class ReducerRegistry<S> {

    val reducers = mutableMapOf<KClass<*>, Reducer<S, Any>>()

    /** Lifts this [Reducer] into the global state via [lens] and registers it. */
    inline fun <reified ACTION : Any, LOCAL> Reducer<LOCAL, ACTION>.scoped(
        lens: Lens<S, LOCAL>,
    ) {
        @Suppress("UNCHECKED_CAST")
        reducers[ACTION::class] = pullback(lens) as Reducer<S, Any>
    }

    /** Registers a [Reducer] that already operates on the global state [S]. */
    inline fun <reified ACTION : Any> Reducer<S, ACTION>.register() {
        @Suppress("UNCHECKED_CAST")
        reducers[ACTION::class] = this as Reducer<S, Any>
    }
}

/** DSL for building the reducer map passed to [io.github.atwa.komposed.createStore]. */
inline fun <S> reducers(build: ReducerRegistry<S>.() -> Unit): Map<KClass<*>, Reducer<S, Any>> =
    ReducerRegistry<S>().apply(build).reducers

/** Collects [EffectHandler]s keyed by their handled effect type. */
class EffectHandlerRegistry {

    val handlers = mutableMapOf<KClass<*>, EffectHandler<Effect, Any>>()

    /** Registers this handler, inferring the effect type [E] from the receiver's type parameter. */
    inline fun <reified E : Effect, A : Any> EffectHandler<E, A>.register() {
        @Suppress("UNCHECKED_CAST")
        handlers[E::class] = this as EffectHandler<Effect, Any>
    }
}

/** DSL for building the effect handler map passed to [io.github.atwa.komposed.createStore]. */
fun effectHandlers(build: EffectHandlerRegistry.() -> Unit): Map<KClass<*>, EffectHandler<Effect, Any>> =
    EffectHandlerRegistry().apply(build).handlers
