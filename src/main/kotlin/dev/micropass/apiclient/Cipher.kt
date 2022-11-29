package dev.micropass.apiclient

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import dev.medzik.libcrypto.AesCbc

/**
 * The Cipher class represents a cipher in the database.
 */
data class Cipher(
    @SerializedName("id") val id: String,
    @SerializedName("favorite") val favorite: Boolean,
    @SerializedName("dir") val directory: String,
    @SerializedName("data") val data: CipherData,
    @SerializedName("createdAt") val createdAt: String = "0",
    @SerializedName("updatedAt") val updatedAt: String = "0"
) {
    companion object {
        /**
         * Deserialize a cipher from a JSON string.
         * @param clearJson The JSON string to deserialize.
         * @return The cipher.
         */
        fun of(clearJson: String): Cipher {
            return Gson().fromJson(clearJson, Cipher::class.java)
        }

        /**
         * Deserialize a cipher from an encrypted JSON string.
         * @param encryptedJson The JSON string to deserialize.
         * @param encryptionKey The encryption key to use.
         * @return The cipher.
         */
        fun of(encryptedJson: String, encryptionKey: String): Cipher {
            val clearJson = AesCbc.decrypt(encryptedJson, encryptionKey)
            return of(clearJson)
        }
    }

    /**
     * Serialize the cipher to a JSON string.
     * @return The JSON string.
     */
    fun toJson(): String {
        return Gson().toJson(this)
    }

    /**
     * Serialize the cipher to an encrypted JSON string.
     * @param encryptionKey The encryption key to use.
     * @return The JSON string.
     */
    fun toJson(encryptionKey: String): String {
        val json = toJson()
        return AesCbc.encrypt(json, encryptionKey)
    }
}

/**
 * The CipherData class represents the data of a cipher.
 */
data class CipherData(
    @SerializedName("type") val type: CipherType,
    @SerializedName("name") val name: String,
    @SerializedName("note") val note: String?,
    @SerializedName("fields") val fields: HashMap<String, CipherField>
)

/**
 * The CipherField class represents a field of a cipher.
 */
data class CipherField(
    @SerializedName("typ") val type: CipherFieldTypes,
    @SerializedName("val") val value: String
)

/**
 * The CipherType enum represents the type of cipher.
 */
enum class CipherType(val type: Number) {
    Login(1),
    SecureNote(2),
    Card(3),
    Identity(4)
}

/**
 * The CipherFieldTypes enum represents the type of field of a cipher.
 */
enum class CipherFieldTypes(val type: Number) {
    Default(-1),
    Text(0),
    Hidden(1),
}
