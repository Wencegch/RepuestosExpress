package com.repuestosexpress.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.repuestosexpress.R
import com.repuestosexpress.models.Pedido
import java.text.SimpleDateFormat
import java.util.Locale

class RecyclerAdapterPedidos(private var listPedidos: ArrayList<Pedido>): RecyclerView.Adapter<RecyclerAdapterPedidos.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null
    private var onItemLongClickListener: RecyclerAdapterProductos.OnItemLongClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pedido, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pedido: Pedido = listPedidos[position]

        // Formatear la fecha y la hora desde el objeto Date
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        val fechaFormatted = dateFormat.format(pedido.fecha)
        val horaFormatted = timeFormat.format(pedido.fecha)

        holder.pedidoFecha.text = fechaFormatted
        holder.pedidoHora.text = horaFormatted
        holder.idPedido.text = "ID: ${pedido.idPedido}"
        holder.estadoPedido.text = pedido.estado

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position)
        }

        // Agregar OnLongClickListener para borrar la familia
        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.onItemLongClick(position)
            true
        }
    }


    override fun getItemCount(): Int {
        return listPedidos.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pedidoFecha: TextView = itemView.findViewById(R.id.txtPedidoFecha)
        val pedidoHora: TextView = itemView.findViewById(R.id.txtPedidoHora)
        val idPedido: TextView = itemView.findViewById(R.id.txtPedidoId)
        val estadoPedido: TextView = itemView.findViewById(R.id.txtPedidoEstado)
    }

    fun getPedidos(pos: Int): Pedido {
        return this.listPedidos[pos]
    }

    //OnItemClickListener
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

    //OnItemLongClickListener
    /**
     * Interfaz para manejar los clics en los elementos del RecyclerView.
     */
    interface OnItemLongClickListener : RecyclerAdapterProductos.OnItemLongClickListener {
        /**
         * Método llamado cuando se hace clic en un elemento del RecyclerView.
         * @param position La posición del elemento en la lista.
         */
        override fun onItemLongClick(position: Int)
    }

    /**
     * Establece el listener para manejar los clics en los elementos del RecyclerView.
     * @param listener El listener para manejar los clics en los elementos.
     */
    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        onItemLongClickListener = listener
    }

}