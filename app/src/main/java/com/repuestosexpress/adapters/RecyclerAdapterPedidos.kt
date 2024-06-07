package com.repuestosexpress.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.repuestosexpress.R
import com.repuestosexpress.models.Pedido
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * RecyclerAdapterPedidos es un adaptador personalizado para un RecyclerView que maneja la lista de pedidos.
 *
 * @param listPedidos Lista de objetos Pedido que representan los diferentes pedidos.
 */
class RecyclerAdapterPedidos(private var listPedidos: ArrayList<Pedido>) : RecyclerView.Adapter<RecyclerAdapterPedidos.ViewHolder>() {

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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pedido, parent, false)
        return ViewHolder(view)
    }

    /**
     * Vincula los datos de un elemento de la lista con una vista.
     *
     * @param holder El ViewHolder que debe ser actualizado para representar los contenidos del elemento en la posición dada.
     * @param position La posición del elemento dentro del adaptador de datos.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pedido: Pedido = listPedidos[position]

        // Formatear la fecha y la hora desde el objeto Date
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        val fechaFormatted = dateFormat.format(pedido.fecha)
        val horaFormatted = timeFormat.format(pedido.fecha)

        holder.idPedido.text = holder.itemView.context.getString(R.string.id, pedido.id)
        holder.estadoPedido.text = pedido.estado
        holder.pedidoFecha.text = fechaFormatted
        holder.pedidoHora.text = horaFormatted

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position)
        }

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
        return listPedidos.size
    }

    /**
     * ViewHolder personalizado que contiene las vistas para cada elemento de la lista.
     *
     * @param itemView La vista para cada elemento.
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pedidoFecha: TextView = itemView.findViewById(R.id.txtPedidoFecha)
        val pedidoHora: TextView = itemView.findViewById(R.id.txtPedidoHora)
        val idPedido: TextView = itemView.findViewById(R.id.txtPedidoId)
        val estadoPedido: TextView = itemView.findViewById(R.id.txtPedidoEstado)
    }

    /**
     * Devuelve el objeto Pedido en la posición especificada.
     *
     * @param pos La posición del elemento.
     * @return El objeto Pedido en la posición especificada.
     */
    fun getPedidos(pos: Int): Pedido {
        return this.listPedidos[pos]
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
    interface OnItemLongClickListener : RecyclerAdapterProductos.OnItemLongClickListener {
        /**
         * Método llamado cuando se hace clic largo en un elemento del RecyclerView.
         * @param position La posición del elemento en la lista.
         */
        override fun onItemLongClick(position: Int)
    }

    /**
     * Establece el listener para manejar los clics largos en los elementos del RecyclerView.
     * @param listener El listener para manejar los clics largos en los elementos.
     */
    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        onItemLongClickListener = listener
    }

    /**
     * Actualiza la lista de pedidos y notifica al adaptador para refrescar los datos.
     *
     * @param newPedidos La nueva lista de pedidos.
     */
    fun updatePedidos(newPedidos: ArrayList<Pedido>) {
        this.listPedidos = newPedidos
        notifyDataSetChanged()
    }
}
