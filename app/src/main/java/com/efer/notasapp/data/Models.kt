package com.efer.notasapp.data

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("token_type")
    val tokenType: String,

    @SerializedName("expires_in")
    val expiresIn: Long,

    val user: AuthUser
)

data class AuthUser(
    val id: String,
    val email: String?
)

data class Note(
    val id: Long? = null,

    @SerializedName("user_id")
    val userId: String? = null,

    val titulo: String,

    val contenido: String,

    @SerializedName("fecha_creacion")
    val fechaCreacion: String? = null
)

data class ApiError(
    val message: String? = null,
    val msg: String? = null,
    val error_description: String? = null
)