package com.repuestosexpress.controllers

import android.os.Bundle
import android.util.Log
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
import com.repuestosexpress.models.LineasPedido
import com.repuestosexpress.models.Producto
import com.repuestosexpress.utils.Firebase
import com.repuestosexpress.utils.Utils

class SeleccionCantidadActivity : AppCompatActivity() {

    private lateinit var progressDrawable: CircularProgressDrawable
    private lateinit var imageProduct: ImageView
    private lateinit var buttonIncrease: Button
    private lateinit var buttonDecrease: Button
    private lateinit var btnComprar: Button
    private lateinit var btnAgregarCarrito: Button
    private lateinit var txtPrecio: TextView
    private lateinit var textQuantity: TextView
    private var quantity: Int = 1
    private var producto: Producto? = null

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
            BeautifulDialog.build(this)
                .title(getString(R.string.realizar_pedido), titleColor = R.color.black)
                .description(getString(R.string.confirmacion_realizar_pedido))
                .type(type = BeautifulDialog.TYPE.INFO)
                .position(BeautifulDialog.POSITIONS.CENTER)
                .onPositive(text = getString(R.string.aceptar), shouldIDismissOnClick = true) {
                    val userUID = Utils.getPreferences(this)
                    Utils.CONTROLAR_PEDIDOS.clear()
                    Utils.CONTROLAR_PEDIDOS.add(LineasPedido(producto?.id!!, quantity))
                    Log.d("CONTROLAR_PEDIDOS", userUID)
                    Firebase().crearPedido(Utils.CONTROLAR_PEDIDOS, userUID)
                    finish()
                }
                .onNegative(text = getString(R.string.cancelar)) {}
        }

        btnAgregarCarrito.setOnClickListener {
            Utils.CONTROLAR_PEDIDOS.add(LineasPedido(producto?.id!!, quantity))
            Utils.Toast(this@SeleccionCantidadActivity, getString(R.string.producto_añadido))
            finish()
        }
    }

    private fun updateQuantityDisplay() {
        textQuantity.text = getString(R.string.cantidad) + quantity
    }

    private fun updatePriceDisplay() {
        val precio = quantity * producto?.precio!!
        txtPrecio.text = getString(R.string.precio_formato2, precio)
    }
}
