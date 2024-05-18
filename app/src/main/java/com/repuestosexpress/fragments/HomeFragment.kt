package com.repuestosexpress.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.repuestosexpress.R
import com.repuestosexpress.adapter.RecyclerAdapterProductos
import com.repuestosexpress.models.Producto
import com.repuestosexpress.utils.Firebase

class HomeFragment : Fragment() {

    private lateinit var sugerenciasAdapter: RecyclerAdapterProductos
    private lateinit var recyclerViewSugerencias: RecyclerView
    private lateinit var sugerencias: ArrayList<Producto>

    private lateinit var novedadesAdapter: RecyclerAdapterProductos
    private lateinit var recyclerViewNovedades: RecyclerView
    private lateinit var novedades: ArrayList<Producto>

    private lateinit var productosTopVentasAdapter: RecyclerAdapterProductos
    private lateinit var productosTopVentas: ArrayList<Producto>
    private lateinit var recyclerViewProductosTopVentas: RecyclerView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sugerencias = ArrayList()
        recyclerViewSugerencias = view.findViewById(R.id.recyclerViewSugerencias)
        recyclerViewSugerencias.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        sugerenciasAdapter = RecyclerAdapterProductos(sugerencias)
        recyclerViewSugerencias.adapter = sugerenciasAdapter

        Firebase().obtenerProductosSugeridos{ listaProductosSugeridos->
            sugerencias.clear()
            sugerencias.addAll(listaProductosSugeridos)
            sugerenciasAdapter.notifyDataSetChanged()
        }

        novedades = ArrayList()
        recyclerViewNovedades = view.findViewById(R.id.recyclerViewNovedades)
        recyclerViewNovedades.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        novedadesAdapter = RecyclerAdapterProductos(novedades)
        recyclerViewNovedades.adapter = novedadesAdapter

        Firebase().obtenerProductosNovedades{ listaProductosNovedades->
            novedades.clear()
            novedades.addAll(listaProductosNovedades)
            novedadesAdapter.notifyDataSetChanged()
        }

        productosTopVentas = ArrayList()
        recyclerViewProductosTopVentas = view.findViewById(R.id.recyclerViewTopVentas)
        recyclerViewProductosTopVentas.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        productosTopVentasAdapter = RecyclerAdapterProductos(productosTopVentas)
        recyclerViewProductosTopVentas.adapter = productosTopVentasAdapter

        Firebase().obtenerProductosTopVentas{ listaProductosTopVentas->
            productosTopVentas.clear()
            productosTopVentas.addAll(listaProductosTopVentas)
            productosTopVentasAdapter.notifyDataSetChanged()
        }


    }

}