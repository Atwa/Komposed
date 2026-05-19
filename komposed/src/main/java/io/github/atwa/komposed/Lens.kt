package io.github.atwa.komposed

/** Pairs a getter and setter for a state slice, enabling zero-boilerplate [pullback] calls. */
class Lens<G, L>(val get: (G) -> L, val set: (G, L) -> G)

/** Creates a [Lens] using a receiver-based setter for concise [copy] calls. */
fun <G, L> lens(get: (G) -> L, set: G.(L) -> G): Lens<G, L> =
    Lens(get = get, set = { global, local -> global.set(local) })
