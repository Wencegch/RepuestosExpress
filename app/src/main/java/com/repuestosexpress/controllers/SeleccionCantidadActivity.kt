package com.repuestosexpress.controllers

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.iamageo.library.BeautifulDialog
import com.iamageo.library.description
import com.iamageo.library.onNegative
import com.iamageo.library.onPositive
import com.iamageo.library.position
import com.iamageo.library.title
import com.iamageo.library.type
import com.repuestosexpress.R
import com.repuestosexpress.components.PaymentShippingDetailsDialog
import com.repuestosexpress.models.LineasPedido
import com.repuestosexpress.models.Producto
import com.repuestosexpress.utils.Firebase
import com.repuestosexpress.utils.Utils

/**
 * SeleccionCantidadActivity es una actividad que permite al usuario seleccionar la cantidad de un producto y realizar una compra.
 */
class SeleccionCantidadActivity : AppCompatActivity(), PaymentShippingDetailsDialog.PaymentShippingDetailsListener {

    private lateinit var progressDrawable: CircularProgressDrawable
    private lateinit var imageProduct: ImageView
    private lateinit var buttonIncrease: Button
    private lateinit var buttonDecrease: Button
    private lateinit var btnComprar: Button
    private lateinit var btnAgregarCarrito: Button
    private lateinit var txtNombreProd: TextView
    private lateinit var txtPrecio: TextView
    private lateinit var textQuantity: TextView
    private var quantity: Int = 1
    private var producto: Producto? = null

    /**
     * Método llamado cuando se crea la actividad.
     * @param savedInstanceState Estado previamente guardado de la actividad.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccion_cantidad)

        // Inicialización de las vistas
        buttonIncrease = findViewById(R.id.buttonIncrease)
        buttonDecrease = findViewById(R.id.buttonDecrease)
        btnComprar = findViewById(R.id.btn_Comprar)
        btnAgregarCarrito = findViewById(R.id.btn_AñadirCesta)
        textQuantity = findViewById(R.id.textQuantityLabel)
        imageProduct = findViewById(R.id.imageViewSeleccionCantidad)
        txtPrecio = findViewById(R.id.txtPrecioCantidad)
        txtNombreProd = findViewById(R.id.txtName)

        producto = intent.getSerializableExtra("Producto") as? Producto

        // Configuración de CircularProgressDrawable
        progressDrawable = CircularProgressDrawable(this).apply {
            setStrokeWidth(10f)
            setStyle(CircularProgressDrawable.LARGE)
            setCenterRadius(30f)
            start()
        }

        // Cargar imagen usando Glide
        producto?.imgUrl?.let { url ->
            Glide.with(this)
                .load(url)
                .placeholder(progressDrawable)
                .error(R.drawable.imagennoencontrada)
                .into(imageProduct)
        }
        txtNombreProd.text = producto?.nombre

        updateQuantityDisplay() // Inicializa la cantidad mostrada
        updatePriceDisplay() // Inicializa el precio mostrado

        // Configurar listeners de los botones
        buttonIncrease.setOnClickListener {
            quantity++
            updateQuantityDisplay()
            updatePriceDisplay()
        }

        buttonDecrease.setOnClickListener {
            if (quantity > 1) {
                quantity--
                updateQuantityDisplay()
                updatePriceDisplay()
            }
        }

        btnComprar.setOnClickListener {
            // Abre el diálogo de detalles de pago y envío
            val dialog = PaymentShippingDetailsDialog()
            dialog.show(supportFragmentManager, "PaymentShippingDialog")
        }

        btnAgregarCarrito.setOnClickListener {
            // Agrega el producto a la cesta de compras
            Utils.LISTA_PEDIDOS.add(LineasPedido(producto?.id!!, quantity, producto?.precio!!))
            Utils.Toast(this@SeleccionCantidadActivity, getString(R.string.producto_añadido))
            finish()
        }
    }

    /**
     * Actualiza la cantidad mostrada en la vista.
     */
    private fun updateQuantityDisplay() {
        textQuantity.text = getString(R.string.cantidad_formato, quantity)
    }

    /**
     * Actualiza el precio mostrado en la vista.
     */
    private fun updatePriceDisplay() {
        val precio = quantity * producto?.precio!!
        txtPrecio.text = getString(R.string.precio_formato2, precio)
    }

    /**
     * Método llamado cuando se confirma el diálogo de detalles de pago y envío.
     * @param address La dirección proporcionada por el usuario.
     * @param paymentMethod El método de pago seleccionado por el usuario.
     */
    override fun onDialogConfirm(address: String, paymentMethod: String) {
        BeautifulDialog.build(this)
            .title(getString(R.string.realizar_pedido), titleColor = R.color.black)
            .description(getString(R.string.confirmacion_realizar_pedido))
            .type(type = BeautifulDialog.TYPE.INFO)
            .position(BeautifulDialog.POSITIONS.CENTER)
            .onPositive(text = getString(android.R.string.ok), shouldIDismissOnClick = true) {
                val userUID = Utils.getPreferences(this)
                Firebase().crearPedidoLinea(LineasPedido(producto?.id!!, quantity, producto?.precio!!), userUID, address, paymentMethod)
                Utils.Toast(this@SeleccionCantidadActivity, getString(R.string.pedido_realizado))
                finish()
            }
            .onNegative(text = getString(android.R.string.cancel)) {}
    }
}
