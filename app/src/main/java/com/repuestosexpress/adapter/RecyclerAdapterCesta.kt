package com.repuestosexpress.adapter

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

class RecyclerAdapterCesta(private var listLineasPedido: ArrayList<LineasPedido>, private val fragment: CestaFragment
) : RecyclerView.Adapter<RecyclerAdapterCesta.ViewHolder>() {

    private lateinit var progressDrawable: CircularProgressDrawable
    private var onItemClickListener: OnItemClickListener? = null
    private var onItemLongClickListener: RecyclerAdapterProductos.OnItemLongClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_linea_pedido, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lineasPedido: LineasPedido = listLineasPedido[position]

        Firebase().obtenerProductoPorId(lineasPedido.idProducto) { lineasPedi ->
            holder.nombreLinea.text = lineasPedi?.nombre
            val precioUnitario = lineasPedi?.precio ?: 0.0
            val precioTotal = precioUnitario * lineasPedido.cantidad
            holder.precioLinea.text = holder.itemView.context.getString(R.string.precio_formato2, precioTotal)
            holder.cantidadLinea.text = holder.itemView.context.getString(R.string.cantidad) + lineasPedido.cantidad
            Glide.with(holder.itemView.context)
                .load(lineasPedi?.imgUrl)
                .placeholder(progressDrawable)
                .error(R.drawable.imagennoencontrada)
                .into(holder.imagenLinea)
        }

        progressDrawable = CircularProgressDrawable(holder.itemView.context)
        progressDrawable.strokeWidth = 10f
        progressDrawable.setStyle(CircularProgressDrawable.LARGE)
        progressDrawable.centerRadius = 30f
        progressDrawable.start()

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position)
        }

        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.onItemLongClick(position)
            true
        }

        holder.textoEliminar.setOnClickListener {
            listLineasPedido.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
            fragment.mostrarPantalla()
        }
    }

    override fun getItemCount(): Int {
        return listLineasPedido.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreLinea: TextView = itemView.findViewById(R.id.txtNombreProductoLinea)
        val precioLinea: TextView = itemView.findViewById(R.id.txtPrecioProductoLinea)
        val cantidadLinea: TextView = itemView.findViewById(R.id.txtCantidadProducto)
        val textoEliminar: TextView = itemView.findViewById(R.id.txtEliminar)
        val imagenLinea: ImageView = itemView.findViewById(R.id.imageViewProductoLinea)
    }

    fun getLineaPedido(pos: Int): LineasPedido {
        return this.listLineasPedido[pos]
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    interface OnItemLongClickListener : RecyclerAdapterProductos.OnItemLongClickListener {
        override fun onItemLongClick(position: Int)
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        onItemLongClickListener = listener
    }
}
