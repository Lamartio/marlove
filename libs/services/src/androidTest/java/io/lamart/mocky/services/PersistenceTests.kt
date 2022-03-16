package io.lamart.mocky.services

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.lamart.mocky.domain.ArtDetails
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class PersistenceTests {

    /**
     * With randomization we are ensured that we assert a new value instead of the previously persisted value.
     */
    private val id = UUID.randomUUID().toString()
    private val details = ArtDetails(
        artObject = ArtDetails.ArtObject(
            id = id,
            labelText = "",
            location = "",
            plaqueDescriptionEnglish = "",
            webImage = null,
            description = null,
            principalMaker = null,
            longTitle = null,
            subTitle = null,

            ),
        artObjectPage = ArtDetails.ArtObjectPage(
            id = "",
            lang = "",
            objectNumber = "",
            plaqueDescription = null
        )
    )

    @Test
    fun persistDetails() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val services = servicesOf(context)

        services.storage.setDetails("1", details)
        assertEquals(details, services.storage.getDetails("1"))
    }

}