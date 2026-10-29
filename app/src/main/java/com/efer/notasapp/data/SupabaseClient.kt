package com.efer.notasapp.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SupabaseClient {


    private const val BASE_URL =
        "https://ncwdmxlvupeqhupzztrj.supabase.co"

    // Clave pública del proyecto Supabase.
// No utilizar aquí una clave service_role o secreta.
    const val API_KEY = "sb_publishable_0tzYBsYin0HNRKyLhDvhgQ_3jF3pOnu"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        // Evita registrar cuerpos de solicitudes y tokens.
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ApiService = retrofit.create(ApiService::class.java)


}

