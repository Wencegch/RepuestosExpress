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
import com.repuestosexpress.adapters.RecyclerAdapterPedidos
import com.repuestosexpress.controllers.DetallePedidoActivity
import com.repuestosexpress.models.Pedido
import com.repuestosexpress.utils.Firebase
import com.repuestosexpress.utils.Utils

/**
 * Fragmento que muestra la lista de pedidos finalizados.
 */
class PedidosFinalizadosFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var pedidosAdapter: RecyclerAdapterPedidos
    private lateinit var pedidos: ArrayList<Pedido>
    private lateinit var pedidosFiltrados: ArrayList<Pedido>
    private lateinit var txtFiltroFinalizado: EditText

    /**
     * Método llamado para crear la vista del fragmento.
     * @param inflater El objeto LayoutInflater que se puede usar para inflar cualquier vista en el fragmento.
     * @param container Si no es nulo, este es el padre de la vista del fragmento.
     * @param savedInstanceState Si no es nulo, este fragmento está siendo reconstruido a partir de un estado guardado previamente como se indicó aquí.
     * @return Retorna la vista raíz del fragmento.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflar el diseño de este fragmento
        return inflater.inflate(R.layout.fragment_pedidos_finalizados, container, false)
    }

    /**
     * Método llamado después de que la vista del fragmento haya sido creada.
     * @param view La vista raíz del fragmento.
     * @param savedInstanceState Si no es nulo, este fragmento está siendo reconstruido a partir de un estado guardado previamente como se indicó aquí.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtFiltroFinalizado = view.findViewById(R.id.txtFiltroFinalizados)
        recyclerView = view.findViewById(R.id.recyclerViewPedidosFinalizados)

        pedidos = ArrayList()
        pedidosFiltrados = ArrayList()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        pedidosAdapter = RecyclerAdapterPedidos(pedidosFiltrados)
        recyclerView.adapter = pedidosAdapter

        txtFiltroFinalizado.addTextChangedListener { userFilter ->
            pedidosFiltrados = pedidos.filter { pedido ->
                pedido.id.lowercase().contains(userFilter.toString().lowercase())
            }.toCollection(ArrayList())
            pedidosAdapter.updatePedidos(pedidosFiltrados)
        }

        Firebase().obtenerPedidosFinalizados(Utils.getPreferences(requireContext())){ listaPedidos ->
            pedidos.clear()
            pedidos.addAll(listaPedidos)
            pedidosFiltrados.clear()
            pedidosFiltrados.addAll(listaPedidos)
            pedidosAdapter.notifyDataSetChanged()
        }

        pedidosAdapter.setOnItemClickListener(object : RecyclerAdapterPedidos.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val pedidoSeleccionado = pedidosFiltrados[position]
                val intent = Intent(requireContext(), DetallePedidoActivity::class.java).apply {
                    putExtra("pedido", pedidoSeleccionado)
                }
                startActivity(intent)
            }
        })
    }
}
