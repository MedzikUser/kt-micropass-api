package dev.micropass.apiclient

import com.google.gson.Gson
import dev.medzik.libcrypto.AesCbc
import dev.medzik.libcrypto.Pbkdf2
import dev.medzik.libcrypto.Salt

/**
 * IdentityApi is a class that provides methods for interacting with the identity API.
 */
object IdentityApi {
    private val client = Client(null)
    private const val iterations = 100000

    /**
     * Create a new account.
     * @param email The email address of the account.
     * @param password The password of the account.
     * @param passwordHint The password hint.
     * @throws Exception If the request fails.
     * @throws Client.Exception If fails to execute the request on the server.
     */
    @Throws(Exception::class, Client.Exception::class)
    fun register(email: String, password: String, passwordHint: String?) {
        val passwordHash = Pbkdf2(iterations).sha256(password, email.toByteArray())
        val passwordHashFinal = Pbkdf2(1).sha512(passwordHash, email.toByteArray())

        val encryptionKey = Pbkdf2(1).sha256(passwordHash, Salt().generate(32))
        val encryptionKeyFinal = AesCbc.encrypt(encryptionKey, passwordHash)

        val body = HashMap<String, String>()
        body["email"] = email
        body["password"] = passwordHashFinal
        body["encryption_key"] = encryptionKeyFinal
        if (passwordHint != null) body["passwordHint"] = passwordHint

        client.post("/identity/register", Gson().toJson(body))
    }

    data class AuthResponse(val accessToken: String, val refreshToken: String?)

    /**
     * Login to an account.
     * @param email The email address of the account.
     * @param password The password of the account.
     * @return [AuthResponse] containing the access and refresh token.
     * @throws Exception If the request fails.
     * @throws Client.Exception If fails to execute the request on the server.
     */
    @Throws(Exception::class, Client.Exception::class)
    fun login(email: String, password: String): AuthResponse {
        val passwordHash = Pbkdf2(iterations).sha256(password, email.toByteArray())
        val passwordHashFinal = Pbkdf2(1).sha512(passwordHash, email.toByteArray())

        val body = HashMap<String, String>()
        body["grant_type"] = "password"
        body["email"] = email
        body["password"] = passwordHashFinal

        val resBody = client.post("/identity/token", Gson().toJson(body))
        val res = Gson().fromJson(resBody, HashMap::class.java)

        return AuthResponse(res["access_token"] as String, res["refresh_token"] as String?)
    }

    /**
     * Refresh the authentication token.
     * @param refreshToken The refresh token.
     * @return [AuthResponse] containing the access token.
     * @throws Exception If the request fails.
     * @throws Client.Exception If fails to execute the request on the server.
     */
    @Throws(Exception::class, Client.Exception::class)
    fun refreshToken(refreshToken: String): AuthResponse {
        val body = HashMap<String, String>()
        body["grant_type"] = "refresh_token"
        body["refresh_token"] = refreshToken

        val resBody = client.post("/identity/token", Gson().toJson(body))
        val res = Gson().fromJson(resBody, HashMap::class.java)

        return AuthResponse(res["access_token"] as String, res["refresh_token"] as String?)
    }
}
