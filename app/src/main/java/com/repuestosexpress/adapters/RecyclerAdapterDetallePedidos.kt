package com.repuestosexpress.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.repuestosexpress.R
import com.repuestosexpress.models.LineasPedido
import com.repuestosexpress.utils.Firebase

/**
 * RecyclerAdapterDetallePedidos es un adaptador personalizado para un RecyclerView que maneja la lista de detalles de pedidos.
 *
 * @param lineasPedidos Lista de objetos LineasPedido que representan los detalles de los productos en un pedido.
 */
class RecyclerAdapterDetallePedidos(private var lineasPedidos: ArrayList<LineasPedido>) : RecyclerView.Adapter<RecyclerAdapterDetallePedidos.ViewHolder>() {

    private lateinit var progressDrawable: CircularProgressDrawable
    private var onItemClickListener: OnItemClickListener? = null
    private var onItemLongClickListener: OnItemLongClickListener? = null

    /**
     * Crea una nueva vista para cada elemento de la lista.
     *
     * @param parent El ViewGroup padre al que la nueva vista será añadida.
     * @param viewType Tipo de la nueva vista.
     * @return Un nuevo ViewHolder que contiene la vista para el elemento.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detalle_pedido, parent, false)
        return ViewHolder(view)
    }

    /**
     * Vincula los datos de un elemento de la lista con una vista.
     *
     * @param holder El ViewHolder que debe ser actualizado para representar los contenidos del elemento en la posición dada.
     * @param position La posición del elemento dentro del adaptador de datos.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lineasPedido: LineasPedido = lineasPedidos[position]

        // Obtiene el producto por ID desde Firebase y actualiza la vista con los datos del producto
        Firebase().obtenerProductoPorId(lineasPedido.idProducto) { producto ->
            if (producto != null) {
                holder.nombreDetallePedido.text = producto.nombre
                val precio = lineasPedido.precio * lineasPedido.cantidad
                holder.precioDetallePedido.text = holder.itemView.context.getString(R.string.precio_formato2, precio)

                // Configuración del CircularProgressDrawable
                progressDrawable = CircularProgressDrawable(holder.itemView.context)
                progressDrawable.setStrokeWidth(10f)
                progressDrawable.setStyle(CircularProgressDrawable.LARGE)
                progressDrawable.setCenterRadius(30f)
                progressDrawable.start()

                Glide.with(holder.itemView.context)
                    .load(producto.imgUrl)
                    .placeholder(progressDrawable)
                    .error(R.drawable.imagennoencontrada)
                    .into(holder.imagenDetallePedido)
            }
        }

        holder.cantidadDetallePedido.text = holder.itemView.context.getString(R.string.cantidad_formato, lineasPedido.cantidad)

        // Configura el listener para clicks normales
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position)
        }

        // Configura el listener para clicks largos
        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.onItemLongClick(position)
            true
        }
    }

    /**
     * Devuelve el número total de elementos en la lista.
     *
     * @return Número total de elementos.
     */
    override fun getItemCount(): Int {
        return lineasPedidos.size
    }

    /**
     * ViewHolder personalizado que contiene las vistas para cada elemento de la lista.
     *
     * @param itemView La vista para cada elemento.
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreDetallePedido: TextView = itemView.findViewById(R.id.txtNombreProductoDetalle)
        val precioDetallePedido: TextView = itemView.findViewById(R.id.txtPrecioProductoDetalle)
        val cantidadDetallePedido: TextView = itemView.findViewById(R.id.txtCantidadDetallePedido)
        val imagenDetallePedido: ImageView = itemView.findViewById(R.id.imageViewProductoDetalle)
    }

    /**
     * Devuelve el objeto LineasPedido en la posición especificada.
     *
     * @param pos La posición del elemento.
     * @return El objeto LineasPedido en la posición especificada.
     */
    fun getLineasPedido(pos: Int): LineasPedido {
        return lineasPedidos[pos]
    }

    /**
     * Interfaz para los callbacks de click en los elementos.
     */
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    /**
     * Establece el listener para los clicks en los elementos.
     *
     * @param listener El listener que manejará los clicks en los elementos.
     */
    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    /**
     * Interfaz para los callbacks de clicks largos en los elementos.
     */
    interface OnItemLongClickListener {
        fun onItemLongClick(position: Int)
    }

    /**
     * Establece el listener para los clicks largos en los elementos.
     *
     * @param listener El listener que manejará los clicks largos en los elementos.
     */
    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        onItemLongClickListener = listener
    }
}
