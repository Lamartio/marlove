package io.lamart.mocky.services

import android.content.Context

/**
 * Interface that gives a project specific access for system service (like; storage, location, bluetooth).
 */

interface Services {
    val storage: Storage
}

fun servicesOf(context: Context): Services =
    object : Services {
        override val storage: Storage = storageOf(context)
    }

