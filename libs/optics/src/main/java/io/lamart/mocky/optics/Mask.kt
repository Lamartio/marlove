package io.lamart.mocky.optics


interface Mask<T , R > {
    fun select(source: T): R?
    fun copy(source: T, focus: R): T
}

fun <T : Any, R : Any> maskOf(select: T.() -> R?, copy: T.(focus: R) -> T): Mask<T, R> =
    object : Mask<T, R> {

        override fun select(source: T): R? = select(source)

        override fun copy(source: T, focus: R): T = copy(source, focus)

    }

fun <T : Any, R : T> prismOf(select: T.() -> R?): Mask<T, R> = maskOf(select) { focus -> focus }