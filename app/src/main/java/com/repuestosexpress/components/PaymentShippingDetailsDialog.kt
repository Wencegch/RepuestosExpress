package com.repuestosexpress.components

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.repuestosexpress.R

/**
 * PaymentShippingDetailsDialog es un fragmento de diálogo que permite al usuario ingresar los detalles de envío y seleccionar un método de pago.
 */
class PaymentShippingDetailsDialog : DialogFragment() {

    /**
     * Interfaz para escuchar los eventos de confirmación del diálogo.
     */
    interface PaymentShippingDetailsListener {
        /**
         * Método llamado cuando el usuario confirma la dirección y el método de pago.
         * @param address La dirección de envío ingresada.
         * @param paymentMethod El método de pago seleccionado.
         */
        fun onDialogConfirm(address: String, paymentMethod: String)
    }

    private var listener: PaymentShippingDetailsListener? = null

    /**
     * Método llamado cuando el fragmento es adjuntado a su contexto.
     * @param context El contexto al que el fragmento está siendo adjuntado.
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is PaymentShippingDetailsListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement PaymentShippingDetailsListener")
        }
    }

    /**
     * Crea y retorna el diálogo del fragmento.
     * @param savedInstanceState Estado previamente guardado del fragmento.
     * @return El diálogo creado.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_envio_y_pago, null)

        val editDireccionEnvio: EditText = view.findViewById(R.id.editDireccionEnvio)
        val spinnerMetodoPago: Spinner = view.findViewById(R.id.spinnerMetodoPago)
        val btnConfirmar: Button = view.findViewById(R.id.btnConfirmar)

        val methods = resources.getStringArray(R.array.metodos_pago)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, methods)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMetodoPago.adapter = adapter

        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        btnConfirmar.setOnClickListener {
            val address = editDireccionEnvio.text.toString()
            if (address.isBlank()) {
                editDireccionEnvio.error = "La dirección no puede estar vacía"
            } else {
                listener?.onDialogConfirm(address, spinnerMetodoPago.selectedItem.toString())
                dialog.dismiss()
            }
        }

        return dialog
    }

    /**
     * Método llamado cuando el fragmento es desadjuntado de su contexto.
     */
    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
