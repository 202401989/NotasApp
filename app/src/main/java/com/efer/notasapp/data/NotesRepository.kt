package com.efer.notasapp.data

class NotesRepository(
    private val sessionManager: SessionManager
) {


    private val api = SupabaseClient.api
    private val apiKey = SupabaseClient.API_KEY

    suspend fun login(
        email: String,
        password: String
    ): Result<AuthUser> {
        return try {
            val response = api.login(
                apiKey = apiKey,
                request = LoginRequest(
                    email = email,
                    password = password
                )
            )

            if (response.isSuccessful) {
                val authResponse = response.body()
                    ?: return Result.failure(
                        Exception("El servidor no devolvió datos de sesión.")
                    )

                sessionManager.saveSession(
                    token = authResponse.accessToken,
                    userId = authResponse.user.id,
                    email = authResponse.user.email
                )

                Result.success(authResponse.user)
            } else {
                Result.failure(
                    Exception(
                        "No se pudo iniciar sesión. " +
                                "Verifica tu correo y contraseña."
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(
                Exception("No se pudo conectar con el servidor: ${e.message}")
            )
        }
    }

    suspend fun getNotes(): Result<List<Note>> {
        return try {
            val token = sessionManager.getToken()
                ?: return Result.failure(
                    Exception("Debes iniciar sesión.")
                )

            val response = api.getNotes(
                apiKey = apiKey,
                authorization = "Bearer $token"
            )

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(
                    Exception("No se pudieron cargar las notas.")
                )
            }
        } catch (e: Exception) {
            Result.failure(
                Exception("Error de conexión: ${e.message}")
            )
        }
    }

    suspend fun createNote(
        titulo: String,
        contenido: String
    ): Result<Note> {
        return try {
            val token = sessionManager.getToken()
                ?: return Result.failure(
                    Exception("Debes iniciar sesión.")
                )

            val userId = sessionManager.getUserId()
                ?: return Result.failure(
                    Exception("No se encontró el usuario autenticado.")
                )

            val response = api.createNote(
                apiKey = apiKey,
                authorization = "Bearer $token",
                note = Note(
                    userId = userId,
                    titulo = titulo,
                    contenido = contenido
                )
            )

            if (response.isSuccessful) {
                val note = response.body()?.firstOrNull()
                    ?: return Result.failure(
                        Exception("La nota no fue devuelta por el servidor.")
                    )

                Result.success(note)
            } else {
                Result.failure(
                    Exception(
                        "No se pudo guardar la nota. " +
                                "Verifica la conexión y los permisos de la base de datos."
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(
                Exception("Error de conexión: ${e.message}")
            )
        }
    }

    fun logout() {
        sessionManager.clearSession()
    }

    fun getCurrentEmail(): String? =
        sessionManager.getUserEmail()


}
