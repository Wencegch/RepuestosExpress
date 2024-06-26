package com.repuestosexpress.controllers

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iamageo.library.BeautifulDialog
import com.iamageo.library.description
import com.iamageo.library.onNegative
import com.iamageo.library.onPositive
import com.iamageo.library.position
import com.iamageo.library.title
import com.iamageo.library.type
import com.repuestosexpress.R
import com.repuestosexpress.adapters.RecyclerAdapterDetallePedidos
import com.repuestosexpress.models.LineasPedido
import com.repuestosexpress.models.Pedido
import com.repuestosexpress.models.Producto
import com.repuestosexpress.utils.Firebase
import com.repuestosexpress.utils.Utils
import java.text.SimpleDateFormat

/**
 * DetallePedidoActivity muestra los detalles de un pedido seleccionado y permite cancelarlo.
 */
class DetallePedidoActivity : AppCompatActivity() {

    private var pedido: Pedido? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var detalleAdapter: RecyclerAdapterDetallePedidos
    private lateinit var btnCancelar: Button
    private lateinit var pedidos: ArrayList<LineasPedido>
    private lateinit var productos: ArrayList<Producto>
    private lateinit var textoPedidoId: TextView
    private lateinit var textoFecha: TextView
    private lateinit var textoEstado: TextView
    private lateinit var textoDireccion: TextView
    private lateinit var textoMetodoPago: TextView
    private lateinit var textoTotal: TextView

    /**
     * Método llamado cuando se crea la actividad.
     * @param savedInstanceState Estado previamente guardado de la actividad.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_pedido)

        textoPedidoId = findViewById(R.id.txtDetallePedidoId)
        textoFecha = findViewById(R.id.txtDetallePedidoFecha)
        textoEstado = findViewById(R.id.txtDetallePedidoEstado)
        textoDireccion = findViewById(R.id.txtDetallePedidoDireccion)
        textoMetodoPago = findViewById(R.id.txtDetallePedidoMetodoPago)
        textoTotal = findViewById(R.id.txtDetallePedidoPrecio)
        btnCancelar = findViewById(R.id.btn_CancelarPedido)
        recyclerView = findViewById(R.id.recyclerViewDetallePedido)

        supportActionBar?.apply {
            title = getString(R.string.detalles_pedido)
            setBackgroundDrawable(ContextCompat.getDrawable(this@DetallePedidoActivity, R.color.green))
        }

        pedidos = ArrayList()
        productos = ArrayList()

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        detalleAdapter = RecyclerAdapterDetallePedidos(pedidos)
        recyclerView.adapter = detalleAdapter

        pedido = intent.getSerializableExtra("pedido") as Pedido

        if (pedido!!.estado == "Finalizado") {
            btnCancelar.visibility = android.view.View.GONE
        }

        Firebase().obtenerLineasDePedido(pedido!!.id) { listaPedidos ->
            pedidos.addAll(listaPedidos)
            textoPedidoId.text = getString(R.string.id, pedido!!.id)
            val fechapar = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(pedido!!.fecha)
            textoFecha.text = getString(R.string.fecha, fechapar)
            textoEstado.text = getString(R.string.estado, pedido!!.estado)
            textoDireccion.text = getString(R.string.direccion, pedido!!.direccion)
            textoMetodoPago.text = getString(R.string.detalle_metodo_pago, pedido!!.metodoPago)
            obtenerProductosParaLineas()
        }

        btnCancelar.setOnClickListener {
            BeautifulDialog.build(this)
                .title(getString(R.string.cancelar_pedido), titleColor = R.color.black)
                .description(getString(R.string.informacion_eliminar_pedido), color = R.color.black)
                .type(type = BeautifulDialog.TYPE.ALERT)
                .position(BeautifulDialog.POSITIONS.CENTER)
                .onPositive(text = getString(android.R.string.ok), shouldIDismissOnClick = true) {
                    Firebase().borrarPedidoPorId(pedido!!.id) { success ->
                        if (success) {
                            Utils.Toast(this, getString(R.string.pedido_cancelado))
                            setResult(RESULT_OK)
                            finish()
                        } else {
                            Utils.Toast(this, getString(R.string.error_eliminar_pedido))
                        }
                    }
                }
                .onNegative(text = getString(android.R.string.cancel)) {}
        }
    }

    /**
     * Obtiene los productos para las líneas de pedido y actualiza el total del pedido.
     */
    private fun obtenerProductosParaLineas() {
        var pendingCallbacks = pedidos.size
        if (pendingCallbacks == 0) {
            detalleAdapter.notifyDataSetChanged()
            return
        }

        var total = 0.0

        for (lineaPedido in pedidos) {
            total += lineaPedido.precio * lineaPedido.cantidad
            pendingCallbacks--
            if (pendingCallbacks == 0) {
                detalleAdapter.notifyDataSetChanged()
                textoTotal.text = getString(R.string.total, total)
            }
        }
    }
}
