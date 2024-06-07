package com.repuestosexpress.controllers

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.repuestosexpress.R
import com.repuestosexpress.fragments.FragmentPreferences

/**
 * PreferencesActivity es una actividad que muestra las preferencias de la aplicación utilizando un fragmento.
 */
class PreferencesActivity : AppCompatActivity() {

    /**
     * Método llamado cuando se crea la actividad.
     * @param savedInstanceState Estado previamente guardado de la actividad.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        // Reemplaza el contenedor de esta ventana por una instancia de FragmentPreferences
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.preferences_container, FragmentPreferences())
            .commit()

        // Habilita el botón de retroceso en la barra de acción
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Carga las preferencias de la aplicación
        loadPreferences()
    }

    /**
     * Método para manejar la selección de elementos del menú.
     * @param item El elemento del menú seleccionado.
     * @return `true` si el evento fue manejado correctamente, `false` en caso contrario.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Método para cargar las preferencias de la aplicación.
     */
    private fun loadPreferences() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val colorFondo = sharedPreferences.getString("colorPreference", "Light")
        when (colorFondo) {
            "Light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "Night" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}
