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
import com.repuestosexpress.adapter.RecyclerAdapterFamilias
import com.repuestosexpress.controllers.ProductosActivity
import com.repuestosexpress.models.Familia
import com.repuestosexpress.utils.Firebase

class RealizarPedidoFragment : Fragment() {

    private lateinit var familiasAdapter: RecyclerAdapterFamilias
    private lateinit var recyclerView: RecyclerView
    private lateinit var familias: ArrayList<Familia>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el diseño del fragmento
        val view = inflater.inflate(R.layout.fragment_realizar_pedido, container, false)

        // Inicializar la lista de familias y el adaptador
        familias = ArrayList()
        recyclerView = view.findViewById(R.id.recyclerViewFamilias)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        familiasAdapter = RecyclerAdapterFamilias(familias)
        recyclerView.adapter = familiasAdapter

        // Obtener las familias desde Firebase
        Firebase().obtenerFamilias { listaFamilias ->
            familias.clear()
            familias.addAll(listaFamilias)
            familiasAdapter.notifyDataSetChanged()
        }

        // Configurar el escucha del clic en los elementos de la lista
        familiasAdapter.setOnItemClickListener(object : RecyclerAdapterFamilias.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val familiaSeleccionada = familias[position]
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
