package com.repuestosexpress.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.repuestosexpress.R
import com.repuestosexpress.adapters.RecyclerAdapterHome
import com.repuestosexpress.controllers.SeleccionCantidadActivity
import com.repuestosexpress.models.Producto
import com.repuestosexpress.utils.Firebase

class HomeFragment : Fragment() {

    private lateinit var sugerenciasAdapter: RecyclerAdapterHome
    private lateinit var recyclerViewSugerencias: RecyclerView
    private lateinit var sugerencias: ArrayList<Producto>

    private lateinit var novedadesAdapter: RecyclerAdapterHome
    private lateinit var recyclerViewNovedades: RecyclerView
    private lateinit var novedades: ArrayList<Producto>

    private lateinit var productosTopVentasAdapter: RecyclerAdapterHome
    private lateinit var productosTopVentas: ArrayList<Producto>
    private lateinit var recyclerViewProductosTopVentas: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar RecyclerViews y adaptadores
        sugerencias = ArrayList()
        recyclerViewSugerencias = view.findViewById(R.id.recyclerViewSugerencias)
        sugerenciasAdapter = configuracionRecyclerView(recyclerViewSugerencias, sugerencias)

        novedades = ArrayList()
        recyclerViewNovedades = view.findViewById(R.id.recyclerViewNovedades)
        novedadesAdapter = configuracionRecyclerView(recyclerViewNovedades, novedades)

        productosTopVentas = ArrayList()
        recyclerViewProductosTopVentas = view.findViewById(R.id.recyclerViewTopVentas)
        productosTopVentasAdapter = configuracionRecyclerView(recyclerViewProductosTopVentas, productosTopVentas)

        // Cargar datos de Firebase
        Firebase().obtenerProductosSugeridos { listaProductosSugeridos ->
            updateAdapterData(sugerencias, listaProductosSugeridos, sugerenciasAdapter)
        }

        Firebase().obtenerProductosNovedades { listaProductosNovedades ->
            updateAdapterData(novedades, listaProductosNovedades, novedadesAdapter)
        }

        Firebase().obtenerProductosTopVentas { listaProductosTopVentas ->
            updateAdapterData(productosTopVentas, listaProductosTopVentas, productosTopVentasAdapter)
        }
    }

    private fun configuracionRecyclerView(recyclerView: RecyclerView, dataList: ArrayList<Producto>): RecyclerAdapterHome {
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val recyclerViewAdapter = RecyclerAdapterHome(dataList)
        recyclerView.adapter = recyclerViewAdapter

        recyclerViewAdapter.setOnItemClickListener(object : RecyclerAdapterHome.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val producto = dataList[position]
                val intent = Intent(requireContext(), SeleccionCantidadActivity::class.java).apply {
                    putExtra("Producto", producto)
                }
                startActivity(intent)
            }
        })

        return recyclerViewAdapter
    }

    private fun updateAdapterData(dataList: ArrayList<Producto>, newData: List<Producto>, adapter: RecyclerAdapterHome) {
        dataList.clear()
        dataList.addAll(newData)
        adapter.notifyDataSetChanged()
    }
}
