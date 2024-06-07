package com.repuestosexpress.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.repuestosexpress.R
import com.repuestosexpress.adapters.RecyclerAdapterCesta
import com.repuestosexpress.components.PaymentShippingDetailsDialog
import com.repuestosexpress.utils.Firebase
import com.repuestosexpress.utils.Utils

/**
 * Fragmento que muestra los elementos agregados a la cesta de compras y permite realizar el pago y el envío de los productos.
 */
class CestaFragment : Fragment(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var cestaAdapter: RecyclerAdapterCesta
    private lateinit var txtsubtotal: TextView
    private lateinit var txtCestaVacia: TextView
    private lateinit var btnTramitarPedido: Button

    /**
     * Método llamado para crear y devolver la jerarquía de vistas asociada con el fragmento.
     * @param inflater El objeto LayoutInflater que puede inflar cualquier vista en el diseño del fragmento.
     * @param container Si no es nulo, es el grupo de vistas al que se agregará el fragmento después de inflar.
     * @param savedInstanceState Si no es nulo, este fragmento está siendo reconstruido a partir de un estado guardado previamente como se indicó aquí.
     * @return Devuelve la jerarquía de vistas asociada con el fragmento.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cesta, container, false)
    }

    /**
     * Método llamado cuando la vista del fragmento se ha creado.
     * @param view La vista del fragmento.
     * @param savedInstanceState Si no es nulo, este fragmento está siendo reconstruido a partir de un estado guardado previamente como se indicó aquí.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicialización de las vistas
        txtsubtotal = view.findViewById(R.id.txtSubtotalCesta)
        txtCestaVacia = view.findViewById(R.id.txtCestaVacia)
        btnTramitarPedido = view.findViewById(R.id.btn_TramitarPedido)
        recyclerView = view.findViewById(R.id.recyclerViewCesta)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        cestaAdapter = RecyclerAdapterCesta(Utils.LISTA_PEDIDOS, this)
        recyclerView.adapter = cestaAdapter

        mostrarPantalla()

        // Configuración del botón para tramitar el pedido
        btnTramitarPedido.setOnClickListener {
            val dialog = PaymentShippingDetailsDialog()
            dialog.setTargetFragment(this, 0)
            dialog.show(parentFragmentManager, "PaymentShippingDialog")
        }
    }

    /**
     * Método para calcular el total de la cesta de compras.
     * @param onTotalCalculated Callback que se invoca cuando se calcula el total.
     */
    fun calcularTotal(onTotalCalculated: (Double) -> Unit) {
        var total = 0.0
        var pendingCallbacks = Utils.LISTA_PEDIDOS.size

        if (pendingCallbacks == 0) {
            onTotalCalculated(total)
            return
        }

        Utils.LISTA_PEDIDOS.forEach { lineasPedido ->
            Firebase().obtenerProductoPorId(lineasPedido.idProducto) { product ->
                if (product != null) {
                    total += (product.precio) * lineasPedido.cantidad
                }
                pendingCallbacks--
                if (pendingCallbacks == 0) {
                    onTotalCalculated(total)
                }
            }
        }
    }

    /**
     * Método para mostrar la pantalla de la cesta de compras.
     */
    fun mostrarPantalla() {
        if (Utils.LISTA_PEDIDOS.isEmpty()) {
            txtsubtotal.visibility = View.GONE
            btnTramitarPedido.visibility = View.GONE
            txtCestaVacia.visibility = View.VISIBLE
        } else {
            calcularTotal { total ->
                txtsubtotal.text = getString(R.string.subtotal, total)
            }
            txtCestaVacia.visibility = View.GONE
            txtsubtotal.visibility = View.VISIBLE
            btnTramitarPedido.visibility = View.VISIBLE
        }
    }
}
