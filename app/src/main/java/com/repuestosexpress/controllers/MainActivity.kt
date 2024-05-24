package com.repuestosexpress.controllers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.iamageo.library.BeautifulDialog
import com.iamageo.library.description
import com.iamageo.library.onNegative
import com.iamageo.library.onPositive
import com.iamageo.library.position
import com.iamageo.library.title
import com.iamageo.library.type
import com.repuestosexpress.fragments.PedidosFragment
import com.repuestosexpress.fragments.HomeFragment
import com.repuestosexpress.R
import com.repuestosexpress.databinding.ActivityMainBinding
import com.repuestosexpress.fragments.CestaFragment
import com.repuestosexpress.fragments.RealizarPedidoFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Reemplaza el fragmento inicial con el fragmento de inicio (HomeFragment)
        replaceFragment(HomeFragment())

        // Establece un listener para el BottomNavigationView
        binding.bottomNavigation.setOnItemSelectedListener {
            // Reemplaza el fragmento actual con el fragmento correspondiente según la opción seleccionada en el BottomNavigationView
            when (it.itemId) {
                R.id.btn_Home -> replaceFragment(HomeFragment())
                R.id.btn_Realizar -> replaceFragment(RealizarPedidoFragment())
                R.id.btn_Cesta -> replaceFragment(CestaFragment())
                R.id.btn_pedidos -> replaceFragment(PedidosFragment())
            }
            true
        }
    }

    //MENÚ SIMPLE
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.simple_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_Settings -> {
                val intent = Intent(this, PreferencesActivity::class.java)
                startActivity(intent)
            }

            R.id.btn_LogOut ->
                BeautifulDialog.build(this)
                    .title(getString(R.string.logout_confirmation), titleColor = R.color.black)
                    .description(getString(R.string.logout_description))
                    .type(type = BeautifulDialog.TYPE.INFO)
                    .position(BeautifulDialog.POSITIONS.CENTER)
                    .onPositive(text = getString(R.string.accept), shouldIDismissOnClick = true) {
                        FirebaseAuth.getInstance().signOut()
                        exit()
                    }
                    .onNegative(text = getString(R.string.cancel)) {}

        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Método llamado cuando se presiona el botón de retroceso.
     */
    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_layout)

        // Si el fragmento actual es BookingsFragment o ProfileFragment, cambia al fragmento HomeFragment
        if (currentFragment is PedidosFragment || currentFragment is CestaFragment || currentFragment is RealizarPedidoFragment) {
            replaceFragment(HomeFragment())
            // Cada vez que se presiona el botón de retroceso, se cambia el elemento seleccionado en el BottomNavigationView al elemento de inicio
            binding.bottomNavigation.selectedItemId = R.id.btn_Home
        } else { onBackPressedDispatcher.onBackPressed() }
    }

    /**
     * Reemplaza el fragmento actual en el contenedor (frame_layout) con el fragmento proporcionado.
     * @param fragment El fragmento que se va a mostrar.
     */
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
    override fun onResume() {
        super.onResume()
        loadPreferences()
    }

    // Cierra la sesión del usuario y borra los ajustes de ese inicio de sesión para evitar entrar directamente
    private fun exit() {
        val prefs = this.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.clear()
        prefs.apply()
        // Navega de vuelta a la actividad de inicio de sesión
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    fun loadPreferences() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val colorFondo = sharedPreferences.getString("preferences_tema", "Light")
        when (colorFondo) {
            "Light" -> getDelegate().localNightMode = AppCompatDelegate.MODE_NIGHT_NO
            "Night" -> getDelegate().localNightMode = AppCompatDelegate.MODE_NIGHT_YES
        }
    }
}