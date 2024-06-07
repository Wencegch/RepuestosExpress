package com.repuestosexpress.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.repuestosexpress.R

/**
 * Fragmento que muestra las preferencias de la aplicación.
 */
class FragmentPreferences : PreferenceFragmentCompat() {

    /**
     * Método llamado para crear el fragmento de preferencias.
     * @param savedInstanceState Si no es nulo, este fragmento está siendo reconstruido a partir de un estado guardado previamente como se indicó aquí.
     * @param rootKey La clave raíz de las preferencias. Si se muestra este fragmento de preferencias dentro de otro fragmento, este es el fragmento raíz; si se muestra como una actividad, es nulo.
     */
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}
