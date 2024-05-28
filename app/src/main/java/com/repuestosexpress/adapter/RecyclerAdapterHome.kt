package com.repuestosexpress.adapter

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

class RecyclerAdapterHome(private var listProductos: ArrayList<Producto>) :RecyclerView.Adapter<RecyclerAdapterHome.ViewHolder>() {

    private lateinit var progressDrawable: CircularProgressDrawable
    private var onItemClickListener: OnItemClickListener? = null
    private var onItemLongClickListener: OnItemLongClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
        return ViewHolder(view)
    }

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

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position)
        }

        // Agregar OnLongClickListener para borrar el producto
        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.onItemLongClick(position)
            true
        }
    }

    override fun getItemCount(): Int {
        return listProductos.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreProductoHome: TextView = itemView.findViewById(R.id.txtNombreProductoHome)
        val precioProductoHome: TextView = itemView.findViewById(R.id.txtPrecioProductoHome)
        val imagenProductoHome: ImageView = itemView.findViewById(R.id.imageViewProductoHome)
    }

    fun getProducto(pos: Int): Producto {
        return listProductos[pos]
    }

    // OnItemClickListener
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    // OnItemLongClickListener
    interface OnItemLongClickListener {
        fun onItemLongClick(position: Int)
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        onItemLongClickListener = listener
    }
}