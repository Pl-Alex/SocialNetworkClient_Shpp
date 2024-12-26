package com.alexp.webapi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("id") val id: Int = 0,
    @SerialName("email") val email: String = "",
    @SerialName("name") val name: String? = "",
    @SerialName("phone") val phone: String? = "",
    @SerialName("address") val address: String? = "",
    @SerialName("career") val career: String? = "",
    @SerialName("birthday") val birthday: String? = "",
    @SerialName("facebook") val facebook: String? = "",
    @SerialName("instagram") val instagram: String? = "",
    @SerialName("twitter") val twitter: String? = "",
    @SerialName("linkedin") val linkedin: String? = "",
    @SerialName("image") val image: String? = "",
    @SerialName("updated_at") val updatedAt: String = "",
    @SerialName("created_at") val createdAt: String = "",
)