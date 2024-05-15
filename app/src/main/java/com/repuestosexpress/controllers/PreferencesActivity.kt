package com.repuestosexpress.controllers

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.repuestosexpress.R

class PreferencesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        //reemplazamos el container de esta ventana por una instancia de fragments
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.preferences_container, FragmentPreferences())
            .commit()

        //boton para atras
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        //cargamos las preferencias
        loadPreferences()
    }

    // Asociamos un oyente al evento de pulsar el icono de volver
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //Para cargar las preferencias
    fun loadPreferences() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val colorFondo = sharedPreferences.getString("preferences_tema", "Light")
        when (colorFondo) {
            "Light" -> getDelegate().localNightMode = AppCompatDelegate.MODE_NIGHT_NO
            "Night" -> getDelegate().localNightMode = AppCompatDelegate.MODE_NIGHT_YES
        }
    }
}