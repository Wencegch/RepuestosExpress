package com.repuestosexpress.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.repuestosexpress.R
import com.repuestosexpress.adapter.RecyclerAdapterPedidos
import com.repuestosexpress.controllers.DetallePedidoActivity
import com.repuestosexpress.models.Pedido
import com.repuestosexpress.utils.Firebase
import com.repuestosexpress.utils.Utils

class PedidosPendientesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var pedidosAdapter: RecyclerAdapterPedidos
    private lateinit var pedidos: ArrayList<Pedido>
    private lateinit var detallePedidoLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pedidos_pendientes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pedidos = ArrayList()
        recyclerView = view.findViewById(R.id.recyclerViewPedidosPendientes)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        pedidosAdapter = RecyclerAdapterPedidos(requireContext(), pedidos)
        recyclerView.adapter = pedidosAdapter

        detallePedidoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                actualizarListaPedidos()
            }
        }

        actualizarListaPedidos()

        pedidosAdapter.setOnItemClickListener(object : RecyclerAdapterPedidos.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val pedidoSeleccionado = pedidos[position]
                val intent = Intent(requireContext(), DetallePedidoActivity::class.java).apply {
                    putExtra("pedido", pedidoSeleccionado)
                }
                detallePedidoLauncher.launch(intent)
            }
        })
    }

    private fun actualizarListaPedidos() {
        Firebase().obtenerPedidosPendientes(Utils.getPreferences(requireContext())) { listaPedidos ->
            pedidos.clear()
            pedidos.addAll(listaPedidos)
            pedidosAdapter.notifyDataSetChanged()
        }
    }
}
