package io.lamart.mocky

import android.app.Application
import io.lamart.mocky.logic.Logic
import io.lamart.mocky.logic.logicOf
import io.lamart.mocky.marlove.marloveOf
import io.lamart.mocky.services.servicesOf
import kotlinx.coroutines.MainScope

class MockyApplication : Application() {

    lateinit var logic: Logic

    override fun onCreate() {
        super.onCreate()
        val scope = MainScope()
        val museum = marloveOf(key = "6fff872af5a76800585c969ac8dff3d6")
        val services = servicesOf(this)

        logic = logicOf(museum, services, scope)

        logic.actions.refresh()
    }

}

val MockyActivity.mockyApplication: MockyApplication
    get() = application as MockyApplication