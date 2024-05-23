package com.repuestosexpress.controllers

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.repuestosexpress.R
import com.repuestosexpress.models.LineasPedido
import com.repuestosexpress.models.Producto
import com.repuestosexpress.utils.Utils

class SeleccionCantidadActivity : AppCompatActivity() {

    private lateinit var progressDrawable: CircularProgressDrawable
    private lateinit var imageProduct: ImageView
    private lateinit var buttonIncrease: Button
    private lateinit var buttonDecrease: Button
    private lateinit var btnComprar: Button
    private lateinit var btnAgregarCarrito: Button
    private lateinit var textQuantity: TextView
    private var quantity: Int = 1

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

        val producto = intent.getSerializableExtra("Producto") as? Producto

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

        // Configurar listeners de los botones
        buttonIncrease.setOnClickListener {
            quantity++
            updateQuantityDisplay()
        }

        buttonDecrease.setOnClickListener {
            if (quantity > 1) {
                quantity--
                updateQuantityDisplay()
            }
        }

        btnComprar.setOnClickListener {

        }

        btnAgregarCarrito.setOnClickListener {
            Utils.CONTROLAR_PEDIDOS.add(LineasPedido(producto?.id!!, quantity))
            Utils.Toast(this@SeleccionCantidadActivity, "Producto agregado al carrito")
            finish()
        }
    }

    private fun updateQuantityDisplay() {
        textQuantity.text = "Cantidad: $quantity"
    }
}
