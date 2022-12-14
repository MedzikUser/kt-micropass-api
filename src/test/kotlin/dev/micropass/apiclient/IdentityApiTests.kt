package dev.micropass.apiclient

import io.github.serpro69.kfaker.Faker
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class IdentityApiTests {
    companion object {
        private val email = "_demo_" + Faker().internet.email()
        private val password = Faker().random.randomString(10)

        lateinit var accessToken: String
        lateinit var refreshToken: String

        @JvmStatic
        @BeforeAll
        fun register() {
            IdentityApi.register(email, password, null)
        }
    }

    @Test
    @BeforeEach
    fun auth() {
        val res = IdentityApi.login(email, password)

        accessToken = res.accessToken
        refreshToken = res.refreshToken!!
    }

    @Test
    fun `invalid password`() {
        try {
            IdentityApi.login(email, "invalid")
        } catch (e: Client.Exception) {
            assertEquals("password_mismatch", e.getResponseError().error)
        }
    }

    @Test
    fun refreshToken() {
        IdentityApi.refreshToken(refreshToken)
    }
}
