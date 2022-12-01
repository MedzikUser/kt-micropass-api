package dev.micropass.apiclient

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.SerializedName
import dev.medzik.libcrypto.AesCbc
import dev.medzik.libcrypto.EncryptException

/**
 * The Cipher class represents a cipher in the database.
 */
data class Cipher(
    @SerializedName("id") val id: String = "",
    @SerializedName("favorite") var favorite: Boolean = false,
    @SerializedName("dir") var directory: String? = null,
    @SerializedName("data") var data: CipherData,
    @SerializedName("created") val createdAt: String? = "0",
    @SerializedName("updated") val updatedAt: String? = "0"
) {
    companion object {
        /**
         * Deserialize a cipher from a JSON string.
         * @param clearJson The JSON string to deserialize.
         * @return The cipher.
         * @throws JsonSyntaxException If the JSON string is invalid.
         */
        @Throws(JsonSyntaxException::class)
        fun of(clearJson: String): Cipher {
            return Gson().fromJson(clearJson, Cipher::class.java)
        }

        /**
         * Deserialize a cipher from an encrypted JSON string.
         * @param encryptedJson The JSON string to deserialize.
         * @param encryptionKey The encryption key to use.
         * @return The cipher.
         * @throws EncryptException If fails to decrypt the JSON.
         * @throws JsonSyntaxException If the JSON string is invalid.
         */
        @Throws(EncryptException::class, JsonSyntaxException::class)
        fun of(encryptedJson: String, encryptionKey: String): Cipher {
            val cipher = Gson().fromJson(encryptedJson, EncryptedCipher::class.java)
            return Cipher(
                id = cipher.id,
                favorite = cipher.favorite,
                directory = cipher.directory,
                data = CipherData.of(AesCbc.decrypt(cipher.data, encryptionKey)),
                createdAt = cipher.createdAt,
                updatedAt = cipher.updatedAt
            )
        }

        /**
         * Deserialize a cipher from an encrypted cipher.
         * @param encCipher The encrypted cipher to deserialize.
         * @param encryptionKey The encryption key to use.
         * @return The cipher.
         * @throws EncryptException If fails to decrypt the JSON.
         * @throws JsonSyntaxException If the JSON string is invalid.
         */
        @Throws(EncryptException::class, JsonSyntaxException::class)
        fun of(encCipher: EncryptedCipher, encryptionKey: String): Cipher {
            return encCipher.toCipher(encryptionKey)
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
     * @throws EncryptException If fails to encrypt the JSON.
     */
    @Throws(EncryptException::class)
    fun toJson(encryptionKey: String): String {
        val encryptedCipher = EncryptedCipher.of(this, encryptionKey)

        return Gson().toJson(encryptedCipher)
    }

    /**
     * The CipherData class represents the data of a cipher.
     */
    data class CipherData(
        @SerializedName("type") var type: Number,
        @SerializedName("name") var name: String,
        @SerializedName("note") var note: String? = null,
        @SerializedName("fields") var fields: HashMap<String, CipherField>
    ) {
        companion object {
            /**
             * Deserialize a cipher data from a JSON string.
             * @param clearJson The JSON string to deserialize.
             * @return The cipher.
             * @throws JsonSyntaxException If the JSON string is invalid.
             */
            @Throws(JsonSyntaxException::class)
            fun of(clearJson: String): CipherData {
                return Gson().fromJson(clearJson, CipherData::class.java)
            }

            /**
             * Deserialize a cipher data from an encrypted JSON string.
             * @param encryptedJson The JSON string to deserialize.
             * @param encryptionKey The encryption key to use.
             * @return The cipher.
             * @throws EncryptException If fails to decrypt the JSON.
             * @throws JsonSyntaxException If the JSON string is invalid.
             */
            @Throws(EncryptException::class, JsonSyntaxException::class)
            fun of(encryptedJson: String, encryptionKey: String): CipherData {
                val clearJson = AesCbc.decrypt(encryptedJson, encryptionKey)
                return Gson().fromJson(clearJson, CipherData::class.java)
            }
        }

        /**
         * Serialize the cipher data to a JSON string.
         * @return The JSON string.
         */
        fun toJson(): String {
            return Gson().toJson(this)
        }

        /**
         * Serialize the cipher data to an encrypted JSON string.
         * @param encryptionKey The encryption key to use.
         * @return The JSON string.
         * @throws EncryptException If fails to encrypt the JSON.
         */
        @Throws(EncryptException::class)
        fun toJson(encryptionKey: String): String {
            val json = toJson()
            return AesCbc.encrypt(json, encryptionKey)
        }
    }

    /**
     * The CipherField class represents a field of a cipher.
     */
    data class CipherField(
        @SerializedName("typ") val type: Number,
        @SerializedName("val") val value: String
    )

    /**
     * The CipherType represents the type of cipher.
     */
    class CipherType {
        companion object {
            const val Login = 1
            const val SecureNote = 2
            const val Card = 3
            const val Identity = 4
        }
    }

    /**
     * The FieldType represents the type of field of a cipher.
     */
    class FieldType {
        companion object {
            const val Default = -1
            const val Text = 0
            const val Hidden = 1
        }
    }
}
