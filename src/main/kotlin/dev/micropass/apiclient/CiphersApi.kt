package dev.micropass.apiclient

import com.google.gson.Gson

class CiphersApi(val encryptionKey: String, val client: Client) {
    companion object {
        fun create(encryptionKey: String, accessToken: String): CiphersApi {
            return CiphersApi(encryptionKey, client = Client(accessToken))
        }
    }

    fun insert(cipher: Cipher) {
        val body = HashMap<String, String>()
        body["data"] = cipher.data.toJson(encryptionKey)

        client.post("/ciphers/insert", body)
    }

    fun update(id: String, cipher: Cipher) {
        val body = HashMap<String, String>()
        body["id"] = id
        body["data"] = cipher.data.toJson(encryptionKey)

        client.patch("/ciphers/update", body)
    }

    fun take(id: String): Cipher {
        val res = client.get("/ciphers/get/$id")
        return Cipher.of(res, encryptionKey)
    }

    fun delete(id: String) {
        client.delete("/ciphers/delete/$id")
    }

    fun list(): CiphersList {
        val res = client.get("/ciphers/list")
        return Gson().fromJson(res, CiphersList::class.java)
    }

    fun list(lastSync: Number): CiphersList {
        val res = client.get("/ciphers/list?lastSync=$lastSync")
        return Gson().fromJson(res, CiphersList::class.java)
    }

    data class CiphersList(val updated: List<String>, val deleted: List<String>)
}
