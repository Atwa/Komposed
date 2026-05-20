package io.github.atwa.komposed

/**
 * An optics pair that focuses on a local state slice [L] within a global state [G].
 *
 * Used by [Reducer.pullback] to lift a reducer that operates on [L] into one that operates
 * on [G], without the local reducer needing any knowledge of the surrounding state shape.
 *
 * @property get extracts the local slice from the global state.
 * @property set returns a new global state with the local slice replaced.
 */
class Lens<G, L>(val get: (G) -> L, val set: (G, L) -> G)

/**
 * Creates a [Lens] using a receiver-based [set] lambda for concise `copy(field = it)` style setters.
 *
 * ```kotlin
 * val cartLens = lens(CheckoutState::cart) { copy(cart = it) }
 * ```
 */
fun <G, L> lens(get: (G) -> L, set: G.(L) -> G): Lens<G, L> =
    Lens(get = get, set = { global, local -> global.set(local) })
