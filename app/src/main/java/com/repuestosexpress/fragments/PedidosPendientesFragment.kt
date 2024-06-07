package com.repuestosexpress.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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
 * Fragmento que muestra una lista de pedidos pendientes.
 * Permite al usuario filtrar los pedidos por su identificador.
 * Los pedidos se muestran en un RecyclerView, donde se puede hacer clic en cada pedido para ver sus detalles.
 */
class PedidosPendientesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var pedidosAdapter: RecyclerAdapterPedidos
    private lateinit var pedidos: ArrayList<Pedido>
    private lateinit var pedidosFiltrados: ArrayList<Pedido>
    private lateinit var detallePedidoLauncher: ActivityResultLauncher<Intent>
    private lateinit var txtFiltroPendiente: EditText

    /**
     * Método llamado para crear la vista del fragmento.
     * @param inflater El objeto LayoutInflater que se puede usar para inflar cualquier vista en el fragmento.
     * @param container Si no es nulo, este es el padre de la vista del fragmento.
     * @param savedInstanceState Si no es nulo, este fragmento está siendo reconstruido a partir de un estado guardado previamente como se indicó aquí.
     * @return Retorna la vista raíz del fragmento.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pedidos_pendientes, container, false)
    }

    /**
     * Método llamado después de que la vista del fragmento haya sido creada.
     * @param view La vista raíz del fragmento.
     * @param savedInstanceState Si no es nulo, este fragmento está siendo reconstruido a partir de un estado guardado previamente como se indicó aquí.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtFiltroPendiente = view.findViewById(R.id.txtFiltroPendientes)
        recyclerView = view.findViewById(R.id.recyclerViewPedidosPendientes)

        pedidos = ArrayList()
        pedidosFiltrados = ArrayList()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        pedidosAdapter = RecyclerAdapterPedidos(pedidosFiltrados)
        recyclerView.adapter = pedidosAdapter

        txtFiltroPendiente.addTextChangedListener { userFilter ->
            pedidosFiltrados = pedidos.filter { pedido ->
                pedido.id.lowercase().contains(userFilter.toString().lowercase())
            }.toCollection(ArrayList())
            pedidosAdapter.updatePedidos(pedidosFiltrados)
        }

        // Inicializar el launcher para el resultado de la actividad de detalle del pedido
        detallePedidoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    actualizarListaPedidos()
                }
            }

        // Actualizar la lista de pedidos pendientes al cargar el fragmento
        actualizarListaPedidos()

        // Establecer el listener de clic en los elementos del RecyclerView
        pedidosAdapter.setOnItemClickListener(object : RecyclerAdapterPedidos.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val pedidoSeleccionado = pedidosFiltrados[position]
                val intent = Intent(requireContext(), DetallePedidoActivity::class.java).apply {
                    putExtra("pedido", pedidoSeleccionado)
                }
                // Iniciar la actividad de detalle del pedido con el launcher
                detallePedidoLauncher.launch(intent)
            }
        })
    }

    /**
     * Método para actualizar la lista de pedidos pendientes obtenidos de Firebase.
     */
    private fun actualizarListaPedidos() {
        Firebase().obtenerPedidosPendientes(Utils.getPreferences(requireContext())) { listaPedidos ->
            pedidos.clear()
            pedidos.addAll(listaPedidos)
            pedidosFiltrados.clear()
            pedidosFiltrados.addAll(listaPedidos)
            pedidosAdapter.notifyDataSetChanged()
        }
    }
}
