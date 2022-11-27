package dev.micropass.apiclient

import com.github.javafaker.Faker
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class IdentityTests {
    companion object {
        @JvmStatic
        private val email = Faker().internet().emailAddress()
        @JvmStatic
        private val password = Faker().internet().password()

        @JvmStatic
        lateinit var accessToken: String
        @JvmStatic
        lateinit var refreshToken: String

        @JvmStatic
        @BeforeAll
        fun register() {
            Identity().register(email, password, null)
        }
    }

    @Test
    @BeforeEach
    fun auth() {
        val res = Identity().login(email, password)

        accessToken = res.accessToken
        refreshToken = res.refreshToken!!
    }

    @Test
    fun refreshToken() {
        Identity().refreshToken(refreshToken)
    }
}
