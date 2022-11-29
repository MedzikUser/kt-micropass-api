package dev.micropass.apiclient

import com.google.gson.annotations.SerializedName

data class EncryptedCipher(
    @SerializedName("id") val id: String = "",
    @SerializedName("favorite") var favorite: Boolean = false,
    @SerializedName("dir") var directory: String?,
    @SerializedName("data") var data: String,
    @SerializedName("created") val createdAt: String?,
    @SerializedName("updated") val updatedAt: String?
)
