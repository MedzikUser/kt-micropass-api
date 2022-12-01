package dev.micropass.apiclient

import com.google.gson.annotations.SerializedName
import dev.medzik.libcrypto.EncryptException

data class EncryptedCipher(
    @SerializedName("id") val id: String = "",
    @SerializedName("favorite") var favorite: Boolean = false,
    @SerializedName("dir") var directory: String?,
    @SerializedName("data") var data: String,
    @SerializedName("created") val createdAt: String?,
    @SerializedName("updated") val updatedAt: String?
) {
    companion object {
        /**
         * Create an encrypted cipher from a cipher.
         * @param cipher The cipher to encrypt.
         * @param encryptionKey The encryption key to use.
         */
        @Throws(EncryptException::class)
        fun of(cipher: Cipher, encryptionKey: String): EncryptedCipher {
            return EncryptedCipher(
                id = cipher.id,
                favorite = cipher.favorite,
                directory = cipher.directory,
                data = cipher.data.toJson(encryptionKey),
                createdAt = cipher.createdAt,
                updatedAt = cipher.updatedAt
            )
        }
    }

    fun toCipher(encryptionKey: String): Cipher {
        return Cipher(
            id = id,
            favorite = favorite,
            directory = directory,
            data = Cipher.CipherData.of(data, encryptionKey),
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
