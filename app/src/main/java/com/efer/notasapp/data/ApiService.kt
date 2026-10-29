package com.efer.notasapp.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {


    // Iniciar sesión con correo y contraseña
    @POST("auth/v1/token")
    suspend fun login(
        @Query("grant_type") grantType: String = "password",
        @Header("apikey") apiKey: String,
        @Body request: LoginRequest
    ): Response<AuthResponse>

    // Obtener las notas del usuario autenticado
    @GET("rest/v1/notas")
    suspend fun getNotes(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Query("select") select: String = "*",
        @Query("order") order: String = "fecha_creacion.desc"
    ): Response<List<Note>>

    // Crear una nota en Supabase
    @POST("rest/v1/notas")
    suspend fun createNote(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Header("Prefer") prefer: String = "return=representation",
        @Body note: Note
    ): Response<List<Note>>


}
