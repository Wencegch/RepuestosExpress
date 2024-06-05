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

class PaymentShippingDetailsDialog : DialogFragment() {

    interface PaymentShippingDetailsListener {
        fun onDialogConfirm(address: String, paymentMethod: String)
    }

    private var listener: PaymentShippingDetailsListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is PaymentShippingDetailsListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement PaymentShippingDetailsListener")
        }
    }

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

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
