package com.repuestosexpress.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.repuestosexpress.R
import com.repuestosexpress.adapter.RecyclerAdapterCesta
import com.repuestosexpress.models.LineasPedido
import com.repuestosexpress.utils.Utils

class CestaFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cestaAdapter: RecyclerAdapterCesta
    private lateinit var pedidos: List<LineasPedido>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cesta, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pedidos = Utils.CONTROLAR_PEDIDOS

        // Usa un forEach para recorrer los pedidos y mostrarlos en el log
        pedidos.forEach { pedido ->
            Log.d("CestaFragment", "Pedido: ${pedido.idProducto}")
        }

        recyclerView = view.findViewById(R.id.recyclerViewCesta)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        cestaAdapter = RecyclerAdapterCesta(Utils.CONTROLAR_PEDIDOS)
        recyclerView.adapter = cestaAdapter
    }

}