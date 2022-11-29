package dev.micropass.apiclient

import io.github.serpro69.kfaker.Faker
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class IdentityApiTests {
    companion object {
        @JvmStatic
        private val email = "_demo_" + Faker().internet.email()
        @JvmStatic
        private val password = Faker().random.randomString(10)

        @JvmStatic
        lateinit var accessToken: String
        @JvmStatic
        lateinit var refreshToken: String

        @JvmStatic
        @BeforeAll
        fun register() {
            IdentityApi().register(email, password, null)
        }
    }

    @Test
    @BeforeEach
    fun auth() {
        val res = IdentityApi().login(email, password)

        accessToken = res.accessToken
        refreshToken = res.refreshToken!!
    }

    @Test
    fun refreshToken() {
        IdentityApi().refreshToken(refreshToken)
    }
}
