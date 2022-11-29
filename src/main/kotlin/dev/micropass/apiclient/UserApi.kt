package dev.micropass.apiclient

import com.google.gson.Gson
import dev.medzik.libcrypto.AesCbc
import dev.medzik.libcrypto.Pbkdf2

class UserApi(accessToken: String) {
    private val client = Client(accessToken)

    /**
     * Get the user encryption key.
     * @param password The password of the user.
     * @param email The email address of the user.
     * @return The user encryption key.
     */
    fun encryptionKey(password: String, email: String): String {
        val resBody = client.get("/user/encryption_key")
        val res = Gson().fromJson(resBody, HashMap::class.java)

        val secretKey = Pbkdf2(100000).sha256(password, email.toByteArray())

        return AesCbc.decrypt(res["encryption_key"] as String, secretKey)
    }

    data class WhoamiResponse(val id: String, val email: String, val username: String)

    /**
     * Get information about the current user.
     * @return [WhoamiResponse], the user information.
     */
    fun whoami(): WhoamiResponse {
        val res = client.get("/user/whoami")

        return Gson().fromJson(res, WhoamiResponse::class.java)
    }
}
