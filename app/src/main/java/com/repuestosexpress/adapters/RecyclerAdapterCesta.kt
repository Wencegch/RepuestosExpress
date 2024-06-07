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
import com.repuestosexpress.fragments.CestaFragment
import com.repuestosexpress.models.LineasPedido
import com.repuestosexpress.utils.Firebase

/**
 * RecyclerAdapterCesta es un adaptador personalizado para un RecyclerView que maneja la lista de líneas de pedido en la cesta de compras.
 *
 * @param listLineasPedido Lista de objetos LineasPedido que representan los productos en la cesta.
 * @param fragment Instancia del fragmento CestaFragment al que pertenece este adaptador.
 */
class RecyclerAdapterCesta(private var listLineasPedido: ArrayList<LineasPedido>, private val fragment: CestaFragment
) : RecyclerView.Adapter<RecyclerAdapterCesta.ViewHolder>() {

    private lateinit var progressDrawable: CircularProgressDrawable
    private var onItemClickListener: OnItemClickListener? = null
    private var onItemLongClickListener: RecyclerAdapterProductos.OnItemLongClickListener? = null

    /**
     * Crea una nueva vista para cada elemento de la lista.
     *
     * @param parent El ViewGroup padre al que la nueva vista será añadida.
     * @param viewType Tipo de la nueva vista.
     * @return Un nuevo ViewHolder que contiene la vista para el elemento.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_linea_pedido, parent, false)
        return ViewHolder(view)
    }

    /**
     * Vincula los datos de un elemento de la lista con una vista.
     *
     * @param holder El ViewHolder que debe ser actualizado para representar los contenidos del elemento en la posición dada.
     * @param position La posición del elemento dentro del adaptador de datos.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lineasPedido: LineasPedido = listLineasPedido[position]

        // Configura el drawable circular de progreso
        progressDrawable = CircularProgressDrawable(holder.itemView.context)
        progressDrawable.strokeWidth = 10f
        progressDrawable.setStyle(CircularProgressDrawable.LARGE)
        progressDrawable.centerRadius = 30f
        progressDrawable.start()

        // Obtiene el producto por ID desde Firebase y actualiza la vista con los datos del producto
        Firebase().obtenerProductoPorId(lineasPedido.idProducto) { lineasPedi ->
            holder.nombreLinea.text = lineasPedi?.nombre
            val precioUnitario = lineasPedi?.precio ?: 0.0
            val precioTotal = precioUnitario * lineasPedido.cantidad
            holder.precioLinea.text = holder.itemView.context.getString(R.string.precio_formato2, precioTotal)
            holder.cantidadLinea.text = holder.itemView.context.getString(R.string.cantidad_formato, lineasPedido.cantidad)

            // Carga la imagen del producto usando Glide
            Glide.with(holder.itemView.context)
                .load(lineasPedi?.imgUrl)
                .placeholder(progressDrawable)
                .error(R.drawable.imagennoencontrada)
                .into(holder.imagenLinea)
        }

        // Configura el listener para clicks normales
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position)
        }

        // Configura el listener para clicks largos
        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.onItemLongClick(position)
            true
        }

        // Configura el listener para eliminar un elemento
        holder.textoEliminar.setOnClickListener {
            listLineasPedido.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
            fragment.mostrarPantalla()
        }
    }

    /**
     * Devuelve el número total de elementos en la lista.
     *
     * @return Número total de elementos.
     */
    override fun getItemCount(): Int {
        return listLineasPedido.size
    }

    /**
     * ViewHolder personalizado que contiene las vistas para cada elemento de la lista.
     *
     * @param itemView La vista para cada elemento.
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreLinea: TextView = itemView.findViewById(R.id.txtNombreProductoLinea)
        val precioLinea: TextView = itemView.findViewById(R.id.txtPrecioProductoLinea)
        val cantidadLinea: TextView = itemView.findViewById(R.id.txtCantidadProducto)
        val textoEliminar: TextView = itemView.findViewById(R.id.txtEliminar)
        val imagenLinea: ImageView = itemView.findViewById(R.id.imageViewProductoLinea)
    }

    /**
     * Devuelve el objeto LineasPedido en la posición especificada.
     *
     * @param pos La posición del elemento.
     * @return El objeto LineasPedido en la posición especificada.
     */
    fun getLineaPedido(pos: Int): LineasPedido {
        return this.listLineasPedido[pos]
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
    interface OnItemLongClickListener : RecyclerAdapterProductos.OnItemLongClickListener {
        override fun onItemLongClick(position: Int)
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
