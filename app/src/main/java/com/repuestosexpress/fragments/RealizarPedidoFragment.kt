package com.repuestosexpress.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.repuestosexpress.R
import com.repuestosexpress.adapters.RecyclerAdapterFamilias
import com.repuestosexpress.controllers.ProductosActivity
import com.repuestosexpress.models.Familia
import com.repuestosexpress.utils.Firebase

/**
 * Fragmento que muestra la lista de familias para realizar un pedido.
 * Permite al usuario filtrar las familias por su nombre.
 * Al hacer clic en una familia, se abre la actividad de productos correspondiente.
 */
class RealizarPedidoFragment : Fragment() {

    private lateinit var familiasAdapter: RecyclerAdapterFamilias
    private lateinit var recyclerView: RecyclerView
    private lateinit var familias: ArrayList<Familia>
    private lateinit var familiasFiltradas: ArrayList<Familia>
    private lateinit var txtFiltroFamilia: EditText

    /**
     * Método llamado para crear la vista del fragmento.
     * @param inflater El objeto LayoutInflater que se puede usar para inflar cualquier vista en el fragmento.
     * @param container Si no es nulo, este es el padre de la vista del fragmento.
     * @param savedInstanceState Si no es nulo, este fragmento está siendo reconstruido a partir de un estado guardado previamente como se indicó aquí.
     * @return Retorna la vista raíz del fragmento.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflar el diseño del fragmento
        val view = inflater.inflate(R.layout.fragment_realizar_pedido, container, false)

        // Inicializar vistas y adaptador
        txtFiltroFamilia = view.findViewById(R.id.txtFiltroFamilia)
        recyclerView = view.findViewById(R.id.recyclerViewFamilias)
        familias = ArrayList()
        familiasFiltradas = ArrayList()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        familiasAdapter = RecyclerAdapterFamilias(familiasFiltradas)
        recyclerView.adapter = familiasAdapter

        // Establecer el listener para el filtro de familias
        txtFiltroFamilia.addTextChangedListener { userFilter ->
            familiasFiltradas = familias.filter { familia ->
                familia.nombre.lowercase().contains(userFilter.toString().lowercase())
            }.toCollection(ArrayList())
            familiasAdapter.updateFamilias(familiasFiltradas)
        }

        // Obtener las familias desde Firebase
        Firebase().obtenerFamilias { listaFamilias ->
            familias.clear()
            familias.addAll(listaFamilias)
            familiasFiltradas.clear()
            familiasFiltradas.addAll(listaFamilias)
            familiasAdapter.notifyDataSetChanged()
        }

        // Configurar el listener para el clic en los elementos de la lista
        familiasAdapter.setOnItemClickListener(object : RecyclerAdapterFamilias.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val familiaSeleccionada = familiasFiltradas[position]
                val intent = Intent(requireContext(), ProductosActivity::class.java).apply {
                    putExtra("Idfamilia", familiaSeleccionada.id)
                    putExtra("Nombre", familiaSeleccionada.nombre)
                }
                startActivity(intent)
            }
        })

        return view
    }
}
