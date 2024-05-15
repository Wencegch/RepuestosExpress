package com.repuestosexpress.controllers

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.repuestosexpress.R


class FragmentPreferences : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}
