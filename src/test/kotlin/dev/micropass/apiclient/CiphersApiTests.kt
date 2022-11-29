package dev.micropass.apiclient

import io.github.serpro69.kfaker.Faker
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class CiphersApiTests {
    companion object {
        @JvmStatic
        private val email = "_demo_" + Faker().internet.email()
        @JvmStatic
        private val password = Faker().random.randomString(10)

        @JvmStatic
        lateinit var accessToken: String
        @JvmStatic
        lateinit var encryptionKey: String

        @JvmStatic
        @BeforeAll
        fun auth() {
            IdentityApi().register(email, password, null)

            val res = IdentityApi().login(email, password)

            accessToken = res.accessToken

            encryptionKey = UserApi(accessToken).encryptionKey(password, email)
        }
    }

    private val client = CiphersApi.create(encryptionKey, accessToken)

    private fun createCipher(): Cipher {
        val cipher = Cipher(
            data = Cipher.CipherData(
                name = Faker().app.name(),
                type = Cipher.CipherType.Login,
                fields = hashMapOf(
                    "user" to Cipher.CipherField(
                        type = Cipher.FieldType.Default,
                        value = Faker().internet.email()
                    ),
                    "pass" to Cipher.CipherField(
                        type = Cipher.FieldType.Default,
                        value = Faker().random.randomString(16)
                    )
                )
            )
        )

        CiphersApi.create(encryptionKey, accessToken).insert(cipher)

        return cipher
    }

    @Test
    fun `insert, list, take, update and delete`() {
        client.insert(createCipher())

        val ciphers = client.list()

        for (cipher in ciphers.updated) {
            val cipherData = client.take(cipher)

            cipherData.data.name = Faker().app.name()

            client.update(cipher, cipherData)

            client.delete(cipher)
        }
    }
}
