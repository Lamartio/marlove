package io.lamart.mocky.optics


interface Source<T : Any> : OptionalSource<T> {

    override fun get(): T

}

fun <T : Any> sourceOf(get: () -> T, set: (value: T) -> Unit): Source<T> =
    object : Source<T> {

        override fun get(): T = get()

        override fun set(value: T) = set(value)

    }

fun <T : Any, R : Any> Source<T>.compose(lens: Lens<T, R>): Source<R> =
    sourceOf(
        get = { this@compose.get().let(lens::select) },
        set = { focus ->
            this@compose
                .get()
                .let { source -> this@compose.set(lens.copy(source, focus)) }
        }
    )