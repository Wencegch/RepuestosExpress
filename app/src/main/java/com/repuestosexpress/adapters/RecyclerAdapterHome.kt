package com.repuestosexpress.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.repuestosexpress.models.Producto
import com.repuestosexpress.R

/**
 * RecyclerAdapterHome es un adaptador personalizado para un RecyclerView que maneja la lista de productos en la pantalla principal.
 *
 * @param listProductos Lista de objetos Producto que representan los diferentes productos.
 */
class RecyclerAdapterHome(private var listProductos: ArrayList<Producto>) : RecyclerView.Adapter<RecyclerAdapterHome.ViewHolder>() {

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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
        return ViewHolder(view)
    }

    /**
     * Vincula los datos de un elemento de la lista con una vista.
     *
     * @param holder El ViewHolder que debe ser actualizado para representar los contenidos del elemento en la posición dada.
     * @param position La posición del elemento dentro del adaptador de datos.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto: Producto = listProductos[position]

        holder.nombreProductoHome.text = producto.nombre
        holder.precioProductoHome.text = holder.itemView.context.getString(R.string.precio_formato, producto.precio)

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
            .into(holder.imagenProductoHome)

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
        return listProductos.size
    }

    /**
     * ViewHolder personalizado que contiene las vistas para cada elemento de la lista.
     *
     * @param itemView La vista para cada elemento.
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreProductoHome: TextView = itemView.findViewById(R.id.txtNombreProductoHome)
        val precioProductoHome: TextView = itemView.findViewById(R.id.txtPrecioProductoHome)
        val imagenProductoHome: ImageView = itemView.findViewById(R.id.imageViewProductoHome)
    }

    /**
     * Devuelve el objeto Producto en la posición especificada.
     *
     * @param pos La posición del elemento.
     * @return El objeto Producto en la posición especificada.
     */
    fun getProducto(pos: Int): Producto {
        return listProductos[pos]
    }

    // OnItemClickListener
    /**
     * Interfaz para manejar los clics en los elementos del RecyclerView.
     */
    interface OnItemClickListener {
        /**
         * Método llamado cuando se hace clic en un elemento del RecyclerView.
         * @param position La posición del elemento en la lista.
         */
        fun onItemClick(position: Int)
    }

    /**
     * Establece el listener para manejar los clics en los elementos del RecyclerView.
     * @param listener El listener para manejar los clics en los elementos.
     */
    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    // OnItemLongClickListener
    /**
     * Interfaz para manejar los clics largos en los elementos del RecyclerView.
     */
    interface OnItemLongClickListener {
        /**
         * Método llamado cuando se hace clic largo en un elemento del RecyclerView.
         * @param position La posición del elemento en la lista.
         */
        fun onItemLongClick(position: Int)
    }

    /**
     * Establece el listener para manejar los clics largos en los elementos del RecyclerView.
     * @param listener El listener para manejar los clics largos en los elementos.
     */
    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        onItemLongClickListener = listener
    }
}
