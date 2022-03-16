package io.lamart.mocky.optics

sealed class Door(open val message: String?) {
    data class Open(override val message: String) : Door(message)
    object Closed : Door(message = null)
}

data class Street(val number: Int, val name: String, val door: Door?) {
    companion object
}

data class Address(val city: String, val street: Street?) {
    companion object
}

data class Company(val name: String, val address: Address) {
    companion object
}

data class Employee(val name: String, val company: Company)