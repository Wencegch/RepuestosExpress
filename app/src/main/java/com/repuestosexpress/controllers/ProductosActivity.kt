package com.repuestosexpress.controllers

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.repuestosexpress.R
import com.repuestosexpress.adapters.RecyclerAdapterProductos
import com.repuestosexpress.models.Producto
import com.repuestosexpress.utils.Firebase

/**
 * ProductosActivity es una actividad que muestra una lista de productos filtrados por una familia específica.
 */
class ProductosActivity : AppCompatActivity() {

    private lateinit var productosAdapter: RecyclerAdapterProductos
    private lateinit var recyclerView: RecyclerView
    private lateinit var productos: ArrayList<Producto>
    private lateinit var productosFiltrados: ArrayList<Producto>
    private lateinit var idFamilia: String
    private lateinit var txtFiltroProducto: EditText

    /**
     * Método llamado cuando se crea la actividad.
     * @param savedInstanceState Estado previamente guardado de la actividad.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        idFamilia = intent.getStringExtra("Idfamilia").toString()
        val nombreFamilia = intent.getStringExtra("Nombre")

        // Configura la barra de acción
        supportActionBar?.apply {
            title = nombreFamilia
            setBackgroundDrawable(ContextCompat.getDrawable(this@ProductosActivity, R.color.green))
        }

        // Inicializa vistas y adaptadores
        txtFiltroProducto = findViewById(R.id.txtFiltroProductos)
        recyclerView = findViewById(R.id.recyclerViewProductos)

        productos = ArrayList()
        productosFiltrados = ArrayList()
        recyclerView.layoutManager = LinearLayoutManager(this)
        productosAdapter = RecyclerAdapterProductos(productosFiltrados)
        recyclerView.adapter = productosAdapter

        // Listener para el filtro de productos
        txtFiltroProducto.addTextChangedListener { userFilter ->
            productosFiltrados = productos.filter { producto ->
                producto.nombre.lowercase().contains(userFilter.toString().lowercase())
            }.toCollection(ArrayList())
            productosAdapter.updateProductos(productosFiltrados)
        }

        // Obtiene los productos de la familia especificada desde Firebase
        Firebase().obtenerProductosFamilia(idFamilia) { listaProductos ->
            productos.clear()
            productos.addAll(listaProductos)
            productosFiltrados.clear()
            productosFiltrados.addAll(listaProductos)
            productosAdapter.notifyDataSetChanged()
        }

        // Listener para los clics en los elementos del RecyclerView
        productosAdapter.setOnItemClickListener(object : RecyclerAdapterProductos.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val productoSeleccionado = productosFiltrados[position]
                // Abre la actividad para seleccionar la cantidad del producto
                val intent = Intent(this@ProductosActivity, SeleccionCantidadActivity::class.java).apply {
                    putExtra("Producto", productoSeleccionado)
                }
                startActivity(intent)
            }
        })
    }
}
