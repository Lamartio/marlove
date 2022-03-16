package io.lamart.mocky.optics


interface OptionalSource<T : Any> {

    fun get(): T?

    fun set(value: T)

}

fun <T : Any> optionalSourceOf(get: () -> T?, set: (value: T) -> Unit): OptionalSource<T> =
    object : OptionalSource<T> {

        override fun get(): T? = get()

        override fun set(value: T) = set(value)

    }

fun <T : Any, R : Any> OptionalSource<T>.compose(mask: Mask<T, R>): OptionalSource<R> =
    optionalSourceOf(
        get = { this@compose.get()?.let(mask::select) },
        set = { focus ->
            this@compose
                .get()
                ?.let { source -> this@compose.set(mask.copy(source, focus)) }
        }
    )

fun <T : Any> OptionalSource<T>.modify(transform: (value: T) -> T) {
    get()
        ?.let(transform)
        ?.let(this::set)
}
