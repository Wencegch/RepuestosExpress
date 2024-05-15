package com.repuestosexpress.controllers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.repuestosexpress.R
import com.repuestosexpress.adapter.RecyclerAdapterProductos
import com.repuestosexpress.models.Producto
import com.repuestosexpress.utils.Firebase

class ProductosActivity : AppCompatActivity() {

    private lateinit var productosAdapter: RecyclerAdapterProductos
    private lateinit var recyclerView: RecyclerView
    private lateinit var productos: ArrayList<Producto>
    private lateinit var idFamilia: String
    private var posicionPulsada: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        idFamilia = intent.getStringExtra("Idfamilia").toString()
        val nombreFamilia = intent.getStringExtra("Nombre")

        supportActionBar?.apply {
            title = nombreFamilia
            setBackgroundDrawable(ContextCompat.getDrawable(this@ProductosActivity, R.color.green))
        }

        productos = ArrayList()
        recyclerView = findViewById(R.id.recyclerViewProductos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        productosAdapter = RecyclerAdapterProductos(productos)
        recyclerView.adapter = productosAdapter

        Firebase().obtenerProductosFamilia(idFamilia) { listaProductos ->
            productos.clear()
            productos.addAll(listaProductos)
            productosAdapter.notifyDataSetChanged()
        }

        productosAdapter.setOnItemLongClickListener(object : RecyclerAdapterProductos.OnItemLongClickListener {
            override fun onItemLongClick(position: Int) {
                // Actualizar la posición pulsada
                posicionPulsada = position
                val productoSeleccionado = productos[posicionPulsada]


            }
        })
    }
}