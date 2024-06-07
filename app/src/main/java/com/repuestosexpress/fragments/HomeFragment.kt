package com.repuestosexpress.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.repuestosexpress.R
import com.repuestosexpress.adapters.RecyclerAdapterHome
import com.repuestosexpress.controllers.SeleccionCantidadActivity
import com.repuestosexpress.models.Producto
import com.repuestosexpress.utils.Firebase

/**
 * Fragmento que muestra la página de inicio de la aplicación.
 */
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

    /**
     * Método llamado para crear la vista del fragmento.
     * @param inflater El objeto LayoutInflater que se puede usar para inflar cualquier vista en el fragmento.
     * @param container Si no es nulo, este es el padre de la vista del fragmento.
     * @param savedInstanceState Si no es nulo, este fragmento está siendo reconstruido a partir de un estado guardado previamente como se indicó aquí.
     * @return Retorna la vista raíz del fragmento.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflar el diseño de este fragmento
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    /**
     * Método llamado después de que la vista del fragmento haya sido creada.
     * @param view La vista raíz del fragmento.
     * @param savedInstanceState Si no es nulo, este fragmento está siendo reconstruido a partir de un estado guardado previamente como se indicó aquí.
     */
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

    /**
     * Configura el RecyclerView y su adaptador.
     * @param recyclerView El RecyclerView a configurar.
     * @param dataList La lista de datos para mostrar en el RecyclerView.
     * @return Retorna el adaptador del RecyclerView.
     */
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

    /**
     * Actualiza los datos del adaptador del RecyclerView.
     * @param dataList La lista de datos actual que se va a actualizar.
     * @param newData La nueva lista de datos.
     * @param adapter El adaptador del RecyclerView a actualizar.
     */
    private fun updateAdapterData(dataList: ArrayList<Producto>, newData: List<Producto>, adapter: RecyclerAdapterHome) {
        dataList.clear()
        dataList.addAll(newData)
        adapter.notifyDataSetChanged()
    }
}
