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

class ProductosActivity : AppCompatActivity() {

    private lateinit var productosAdapter: RecyclerAdapterProductos
    private lateinit var recyclerView: RecyclerView
    private lateinit var productos: ArrayList<Producto>
    private lateinit var productosFiltrados: ArrayList<Producto>
    private lateinit var idFamilia: String
    private lateinit var txtFiltroProducto: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        idFamilia = intent.getStringExtra("Idfamilia").toString()
        val nombreFamilia = intent.getStringExtra("Nombre")

        supportActionBar?.apply {
            title = nombreFamilia
            setBackgroundDrawable(ContextCompat.getDrawable(this@ProductosActivity, R.color.green))
        }

        txtFiltroProducto = findViewById(R.id.txtFiltroProductos)
        recyclerView = findViewById(R.id.recyclerViewProductos)

        productos = ArrayList()
        productosFiltrados = ArrayList()
        recyclerView.layoutManager = LinearLayoutManager(this)
        productosAdapter = RecyclerAdapterProductos(productosFiltrados)
        recyclerView.adapter = productosAdapter

        txtFiltroProducto.addTextChangedListener { userFilter ->
            productosFiltrados = productos.filter { producto ->
                producto.nombre.lowercase().contains(userFilter.toString().lowercase())
            }.toCollection(ArrayList())
            productosAdapter.updateProductos(productosFiltrados)
        }

        Firebase().obtenerProductosFamilia(idFamilia) { listaProductos ->
            productos.clear()
            productos.addAll(listaProductos)
            productosFiltrados.clear()
            productosFiltrados.addAll(listaProductos)
            productosAdapter.notifyDataSetChanged()
        }

        productosAdapter.setOnItemClickListener(object : RecyclerAdapterProductos.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val productoSeleccionado = productosFiltrados[position]
                val intent = Intent(this@ProductosActivity, SeleccionCantidadActivity::class.java).apply {
                    putExtra("Producto", productoSeleccionado)
                }
                startActivity(intent)
            }
        })
    }
}
