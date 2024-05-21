package com.repuestosexpress.utils

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import com.repuestosexpress.R
import com.repuestosexpress.models.LineasPedido

class Utils {
    companion object{
        const val RC_SIGN_IN = 9001

        /**
         * Método para mostrar un mensaje Toast.
         * @param cont Contexto en el que se muestra el Toast.
         * @param sms Mensaje a mostrar en el Toast.
         */
        fun Toast(cont: Context, sms: String) {
            android.widget.Toast.makeText(cont, sms, android.widget.Toast.LENGTH_SHORT).show()
        }

        fun createUserPrefs(cont: Context, user: FirebaseUser?) {
            // Obtiene una instancia del objeto SharedPreferences.Editor para realizar cambios en las preferencias compartidas
            val prefs =
                cont.getSharedPreferences(cont.getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()

            // Almacena el nombre de usuario, el correo electrónico y el identificador único del usuario actual, si están disponibles
            // bajo las claves "name", "email" y "UID" respectivamente
            prefs.putString("name", user?.displayName)
            prefs.putString("email", user?.email)
            prefs.putString("UID", user?.uid)

            // Aplica los cambios en el editor de preferencias compartidas para escribir los datos en el archivo de preferencias compartidas de forma asíncrona
            prefs.apply()
        }

        var CONTROLAR_PEDIDOS: ArrayList<LineasPedido> = ArrayList()
    }
}