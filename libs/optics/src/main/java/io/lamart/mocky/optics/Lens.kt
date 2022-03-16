package io.lamart.mocky.optics

interface Lens<T : Any, R : Any> : Mask<T, R> {

    override fun select(source: T): R

}

fun <T : Any, R : Any> lensOf(select: T.() -> R, copy: T.(focus: R) -> T): Lens<T, R> =
    object : Lens<T, R> {

        override fun copy(source: T, focus: R): T = copy(source, focus)

        override fun select(source: T): R = select(source)

    }
