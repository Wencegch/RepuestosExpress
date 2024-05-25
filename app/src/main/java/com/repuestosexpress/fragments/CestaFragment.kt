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
import com.repuestosexpress.adapter.RecyclerAdapterCesta
import com.repuestosexpress.utils.Firebase
import com.repuestosexpress.utils.Utils

class CestaFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cestaAdapter: RecyclerAdapterCesta
    private lateinit var txtsubtotal: TextView
    private lateinit var txtTotal: TextView
    private lateinit var txtCestaVacia: TextView
    private lateinit var btnTramitarPedido: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cesta, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtTotal = view.findViewById(R.id.txtPrecioTotal)
        txtsubtotal = view.findViewById(R.id.txtSubtotalCesta)
        txtCestaVacia = view.findViewById(R.id.txtCestaVacia)
        btnTramitarPedido = view.findViewById(R.id.btn_TramitarPedido)
        recyclerView = view.findViewById(R.id.recyclerViewCesta)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        cestaAdapter = RecyclerAdapterCesta(Utils.CONTROLAR_PEDIDOS, this)
        recyclerView.adapter = cestaAdapter

        mostrarPantalla()
    }

    fun calcularTotal(onTotalCalculated: (Double) -> Unit) {
        var total = 0.0
        var pendingCallbacks = Utils.CONTROLAR_PEDIDOS.size

        if (pendingCallbacks == 0) {
            onTotalCalculated(total)
            return
        }

        Utils.CONTROLAR_PEDIDOS.forEach { lineasPedido ->
            Firebase().obtenerProductoPorId(lineasPedido.idProducto) { product ->
                if (product != null) {
                    total += (product.precio ?: 0.0) * lineasPedido.cantidad
                }
                pendingCallbacks--
                if (pendingCallbacks == 0) {
                    onTotalCalculated(total)
                }
            }
        }
    }

    fun mostrarPantalla() {
        if (Utils.CONTROLAR_PEDIDOS.isEmpty()) {
            txtsubtotal.visibility = View.GONE
            txtTotal.visibility = View.GONE
            btnTramitarPedido.visibility = View.GONE
            txtCestaVacia.visibility = View.VISIBLE
        } else {
            calcularTotal { total ->
                txtTotal.text = getString(R.string.precio_formato, total)
            }
            txtCestaVacia.visibility = View.GONE
            txtsubtotal.visibility = View.VISIBLE
            txtTotal.visibility = View.VISIBLE
            btnTramitarPedido.visibility = View.VISIBLE
        }
    }
}
