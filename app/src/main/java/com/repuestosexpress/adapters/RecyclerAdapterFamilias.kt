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
import com.repuestosexpress.models.Familia

/**
 * RecyclerAdapterFamilias es un adaptador personalizado para un RecyclerView que maneja la lista de familias de productos.
 *
 * @param listFamilias Lista de objetos Familia que representan las diferentes familias de productos.
 */
class RecyclerAdapterFamilias(private var listFamilias: ArrayList<Familia>): RecyclerView.Adapter<RecyclerAdapterFamilias.ViewHolder>() {

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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_familia, parent, false)
        return ViewHolder(view)
    }

    /**
     * Vincula los datos de un elemento de la lista con una vista.
     *
     * @param holder El ViewHolder que debe ser actualizado para representar los contenidos del elemento en la posición dada.
     * @param position La posición del elemento dentro del adaptador de datos.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val familia: Familia = listFamilias[position]

        holder.nombre.text = familia.nombre
        holder.info.text = familia.info

        // Configuración del CircularProgressDrawable
        progressDrawable = CircularProgressDrawable(holder.itemView.context)
        progressDrawable.setStrokeWidth(10f)
        progressDrawable.setStyle(CircularProgressDrawable.LARGE)
        progressDrawable.setCenterRadius(30f)
        progressDrawable.start()

        Glide.with(holder.itemView.context)
            .load(familia.imgUrl)
            .placeholder(progressDrawable)
            .error(R.drawable.imagennoencontrada)
            .into(holder.imagenFamilia)

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
        return listFamilias.size
    }

    /**
     * ViewHolder personalizado que contiene las vistas para cada elemento de la lista.
     *
     * @param itemView La vista para cada elemento.
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.txtNombreFamilia)
        val info: TextView = itemView.findViewById(R.id.txtInformacion)
        val imagenFamilia: ImageView = itemView.findViewById(R.id.imageViewFamilia)
    }

    /**
     * Devuelve el objeto Familia en la posición especificada.
     *
     * @param pos La posición del elemento.
     * @return El objeto Familia en la posición especificada.
     */
    fun getFamilia(pos: Int): Familia {
        return this.listFamilias[pos]
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
     * Actualiza la lista de familias con nuevos datos y notifica al adaptador de cambios.
     * @param newFamilias La nueva lista de familias.
     */
    fun updateFamilias(newFamilias: ArrayList<Familia>) {
        this.listFamilias = newFamilias
        notifyDataSetChanged()
    }
}
