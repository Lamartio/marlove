package io.lamart.mocky.optics

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class Optics {

    private val initial = Employee("Danny",
        Company("Lamarti",
            Address("Uddel",
                Street(13, "Vossen",
                    Door.Open("welcome")
                )
            )
        )
    )

    @Test
    fun composeAndModifyAHugeState() {
        val values = mutableListOf<Employee>(initial)
        val source = sourceOf({ values.last() }, values::add)

        source
            // composition allows to work with sub-states
            .compose(lensOf({ this.company }, { company -> this.copy(company = company) }))
            // same as the line above, but than in shorthand
            .compose(lensOf({ address }, { copy(address = it) }))
            // mask is different than lens, since street is an optional. Meaning that setting the editing the street can sometimes not happen.
            .compose(maskOf({ street }, { copy(street = it) }))
            .compose(maskOf({ door }, { copy(door = it) }))
            // a shorthand to work with diverging states (sealed classes, enums)
            .compose(prismOf { this as? Door.Open })
            .compose(maskOf({ message }, { copy(message = it) }))
            // get, set or modify the composed state
            .modify { message -> "$message!!!" }

        assertThat(values.first().company.address.street?.door?.message)
            .isEqualTo("welcome")

        assertThat(values.last().company.address.street?.door?.message)
            .isEqualTo("welcome!!!")
    }

}