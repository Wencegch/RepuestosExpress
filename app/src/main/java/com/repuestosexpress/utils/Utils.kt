package com.repuestosexpress.utils

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import com.repuestosexpress.R
import com.repuestosexpress.models.LineasPedido

class Utils {
    companion object {
        /**
         * Código de solicitud utilizado para el inicio de sesión con Firebase Authentication.
         */
        const val RC_SIGN_IN = 9001

        /**
         * Muestra un mensaje Toast en la interfaz de usuario.
         *
         * @param cont Contexto en el que se muestra el Toast.
         * @param sms Mensaje a mostrar en el Toast.
         */
        fun Toast(cont: Context, sms: String) {
            android.widget.Toast.makeText(cont, sms, android.widget.Toast.LENGTH_SHORT).show()
        }

        /**
         * Crea o actualiza las preferencias compartidas del usuario con la información proporcionada.
         *
         * @param cont Contexto de la aplicación.
         * @param user Usuario actual autenticado con Firebase.
         */
        fun createUserPrefs(cont: Context, user: FirebaseUser?) {
            val prefs = cont.getSharedPreferences(cont.getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.putString("name", user?.displayName)
            prefs.putString("email", user?.email)
            prefs.putString("UID", user?.uid)
            prefs.apply()
        }

        /**
         * Lista mutable que contiene los elementos de la línea de pedidos del usuario.
         */
        var LISTA_PEDIDOS: ArrayList<LineasPedido> = ArrayList()

        /**
         * Obtiene el UID del usuario actual almacenado en las preferencias compartidas.
         *
         * @param cont Contexto de la aplicación.
         * @return El UID del usuario actual o una cadena vacía si no se encuentra.
         */
        fun getPreferences(cont: Context): String {
            val prefs = cont.applicationContext.getSharedPreferences(cont.getString(R.string.prefs_file), Context.MODE_PRIVATE)
            return prefs.getString("UID", "").toString()
        }
    }
}