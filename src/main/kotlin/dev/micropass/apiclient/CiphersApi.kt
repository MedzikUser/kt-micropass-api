package dev.micropass.apiclient

import com.google.gson.Gson

class CiphersApi(private val encryptionKey: String, val client: Client) {
    companion object {
        fun create(encryptionKey: String, accessToken: String): CiphersApi {
            return CiphersApi(encryptionKey, client = Client(accessToken))
        }
    }

    /**
     * Insert the cipher into the database.
     * @param cipher The cipher to insert.
     * @throws Exception If fails to insert the cipher.
     */
    @Throws(Exception::class)
    fun insert(cipher: Cipher) {
        val body = HashMap<String, String>()
        body["data"] = cipher.data.toJson(encryptionKey)

        client.post("/ciphers/insert", body)
    }

    /**
     * Update the cipher in the database.
     * @param id The id of the cipher to update.
     * @param cipher The cipher to update.
     * @throws Exception If fails to update the cipher.
     */
    fun update(id: String, cipher: Cipher) {
        val body = HashMap<String, String>()
        body["id"] = id
        body["data"] = cipher.data.toJson(encryptionKey)

        client.patch("/ciphers/update", body)
    }

    /**
     * Take the cipher out of the database.
     * @param id The id of the cipher to delete.
     * @throws Exception If fails to take the cipher.
     */
    fun take(id: String): Cipher {
        val res = client.get("/ciphers/get/$id")
        return Cipher.of(res, encryptionKey)
    }

    /**
     * Delete the cipher from the database.
     * @param id The id of the cipher to delete.
     * @throws Exception If fails to delete the cipher.
     */
    fun delete(id: String) {
        client.delete("/ciphers/delete/$id")
    }

    /**
     * Get all ciphers from the database.
     * @return [CiphersList] - The list of ciphers.
     * @throws Exception If fails to get the ciphers.
     */
    fun list(): CiphersList {
        val res = client.get("/ciphers/list")
        return Gson().fromJson(res, CiphersList::class.java)
    }

    /**
     * Get all ciphers updated or deleted after last sync time.
     * @returns [CiphersList] The list of ciphers.
     * @throws Exception If fails to get the ciphers.
     */
    fun list(lastSync: Number): CiphersList {
        val res = client.get("/ciphers/list?lastSync=$lastSync")
        return Gson().fromJson(res, CiphersList::class.java)
    }

    /**
     * Ciphers list response.
     * @property updated The ciphers are updated.
     * @property deleted The ciphers are deleted.
     */
    data class CiphersList(val updated: List<String>, val deleted: List<String>)
}
