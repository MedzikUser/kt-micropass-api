package dev.micropass.apiclient

import io.github.serpro69.kfaker.Faker
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class IdentityTests {
    companion object {
        @JvmStatic
        private val email = Faker().internet.email()
        @JvmStatic
        private val password = Faker().random.randomString(10)

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
