package com.efer.notasapp.data

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {


    private val preferences: SharedPreferences =
        context.getSharedPreferences("notas_session", Context.MODE_PRIVATE)

    fun saveSession(token: String, userId: String, email: String?) {
        preferences.edit()
            .putString("access_token", token)
            .putString("user_id", userId)
            .putString("user_email", email)
            .apply()
    }

    fun getToken(): String? =
        preferences.getString("access_token", null)

    fun getUserId(): String? =
        preferences.getString("user_id", null)

    fun getUserEmail(): String? =
        preferences.getString("user_email", null)

    fun clearSession() {
        preferences.edit().clear().apply()
    }


}
