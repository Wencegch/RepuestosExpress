package com.repuestosexpress.controllers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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
import com.repuestosexpress.components.PaymentShippingDetailsDialog
import com.repuestosexpress.databinding.ActivityMainBinding
import com.repuestosexpress.fragments.CestaFragment
import com.repuestosexpress.fragments.RealizarPedidoFragment
import com.repuestosexpress.utils.Firebase
import com.repuestosexpress.utils.Utils

/**
 * MainActivity es la actividad principal de la aplicación, que muestra diferentes fragmentos según la selección del usuario.
 */
class MainActivity : AppCompatActivity(), PaymentShippingDetailsDialog.PaymentShippingDetailsListener {
    private lateinit var binding: ActivityMainBinding

    /**
     * Método llamado cuando se crea la actividad.
     * @param savedInstanceState Estado previamente guardado de la actividad.
     */
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

    /**
     * Método llamado cuando se selecciona un elemento del menú.
     * @param item El elemento del menú seleccionado.
     * @return `true` si el evento fue manejado correctamente, `false` en caso contrario.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_LogOut -> {
                mostrarDialogConfirmacionSalida()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Método para manejar el botón de retroceso.
     */
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_layout)

        // Si el fragmento actual es PedidosFragment, CestaFragment o RealizarPedidoFragment, cambia al fragmento HomeFragment
        if (currentFragment is PedidosFragment || currentFragment is CestaFragment || currentFragment is RealizarPedidoFragment) {
            replaceFragment(HomeFragment())
            // Cada vez que se presiona el botón de retroceso, se cambia el elemento seleccionado en el BottomNavigationView al elemento de inicio
            binding.bottomNavigation.selectedItemId = R.id.btn_Home
        } else {
            super.onBackPressed()
        }
    }

    /**
     * Método para reemplazar un fragmento en la actividad.
     * @param fragment El fragmento que se va a mostrar.
     */
    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    /**
     * Método para manejar el proceso de cierre de sesión.
     */
    private fun mostrarDialogConfirmacionSalida() {
        BeautifulDialog.build(this)
            .title(getString(R.string.logout_confirmation), titleColor = R.color.black)
            .description(getString(R.string.logout_description))
            .type(BeautifulDialog.TYPE.INFO)
            .position(BeautifulDialog.POSITIONS.CENTER)
            .onPositive(text = getString(android.R.string.ok), shouldIDismissOnClick = true) {
                logout()
            }
            .onNegative(text = getString(android.R.string.cancel)) {}
    }

    /**
     * Método para cerrar la sesión del usuario actual y redirigirlo a la pantalla de inicio de sesión.
     */
    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        clearSharedPreferences()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Método para limpiar las preferencias compartidas.
     */
    private fun clearSharedPreferences() {
        val prefs = this.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.clear()
        prefs.apply()
    }

    override fun onDialogConfirm(address: String, paymentMethod: String) {
        BeautifulDialog.build(this)
            .title(getString(R.string.realizar_pedido), titleColor = R.color.black)
            .description(getString(R.string.confirmacion_realizar_pedido))
            .type(type = BeautifulDialog.TYPE.INFO)
            .position(BeautifulDialog.POSITIONS.CENTER)
            .onPositive(text = getString(android.R.string.ok), shouldIDismissOnClick = true) {
                val userUID = Utils.getPreferences(this)
                Firebase().crearPedido(Utils.LISTA_PEDIDOS, userUID, address, paymentMethod) {
                    Utils.Toast(this, getString(R.string.pedido_realizado))
                    val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_layout)
                    if (currentFragment is CestaFragment) {
                        currentFragment.mostrarPantalla()
                    }
                }
            }
            .onNegative(text = getString(android.R.string.cancel)) {}
    }
}
