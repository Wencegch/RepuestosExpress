package com.repuestosexpress.utils

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.repuestosexpress.models.Familia
import com.repuestosexpress.models.LineasPedido
import com.repuestosexpress.models.Pedido
import com.repuestosexpress.models.Producto
import java.util.Calendar

class Firebase {
    private var referenceFamilias = FirebaseFirestore.getInstance().collection("Familias")
    private var referenceProductos = FirebaseFirestore.getInstance().collection("Productos")
    private var referencePedidos = FirebaseFirestore.getInstance().collection("Pedidos")
    fun obtenerFamilias(onComplete: (List<Familia>) -> Unit) {
        val listaFamilias = mutableListOf<Familia>()
        referenceFamilias.get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                val id = document.id
                val nombre = document.getString("nombre")
                val info = document.getString("info")
                val imgUrl = document.getString("imgUrl")
                if (nombre != null && info != null && imgUrl != null) {
                    val familia = Familia(id, nombre, info, imgUrl)
                    listaFamilias.add(familia)
                }
            }
            onComplete(listaFamilias)
        }.addOnFailureListener { exception ->
            Log.d("Error", "$exception")
            onComplete(emptyList()) // Devolver una lista vacía en caso de error
        }
    }

    fun obtenerProductosFamilia(idFamilia: String, onComplete: (List<Producto>) -> Unit) {
        val listaProductos = mutableListOf<Producto>()
        referenceProductos.whereEqualTo("idFamilia", idFamilia).get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val idFirebase = document.id
                    val nombre = document.getString("nombre")
                    val precio = document.getDouble("precio")
                    val imgUrl = document.getString("imgUrl")
                    val idFamilia = document.getString("idFamilia")

                    if (nombre != null && precio != null && imgUrl != null && idFamilia != null) {
                        val producto =
                            Producto(idFirebase, nombre, precio.toDouble(), imgUrl, idFamilia)
                        listaProductos.add(producto)
                    }
                }
                onComplete(listaProductos)
            }.addOnFailureListener { exception ->
                Log.d("Error", "$exception")
                onComplete(emptyList()) // Devolver una lista vacía en caso de error
            }
    }

    fun obtenerProductosSugeridos(onComplete: (List<Producto>) -> Unit) {
        val listaProductosSugeridos = mutableListOf<Producto>()

        referenceProductos.whereEqualTo("sugerencias", true)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val idProducto = document.id
                    val nombre = document.getString("nombre")
                    val precio = document.getDouble("precio")
                    val imgUrl = document.getString("imgUrl")
                    val idFamilia = document.getString("idFamilia")

                    if (nombre != null && precio != null && imgUrl != null && idFamilia != null) {
                        val producto =
                            Producto(idProducto, nombre, precio.toDouble(), imgUrl, idFamilia)
                        listaProductosSugeridos.add(producto)
                    }
                }
                onComplete(listaProductosSugeridos)
            }
            .addOnFailureListener { exception ->
                Log.e("Error", "Error al obtener productos sugeridos: $exception")
                onComplete(emptyList()) // Devolver una lista vacía en caso de error
            }
    }

    fun obtenerProductosNovedades(onComplete: (List<Producto>) -> Unit) {
        val listaNovedades = mutableListOf<Producto>()

        referenceProductos.orderBy("fecha", Query.Direction.DESCENDING) // Ordenar por fecha en orden descendente
            .limit(4) // Limitar la cantidad de resultados a 4
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val idProducto = document.id
                    val nombre = document.getString("nombre")
                    val precio = document.getDouble("precio")
                    val imgUrl = document.getString("imgUrl")
                    val idFamilia = document.getString("idFamilia")

                    if (nombre != null && precio != null && imgUrl != null && idFamilia != null) {
                        val producto =
                            Producto(idProducto, nombre, precio.toDouble(), imgUrl, idFamilia)
                        listaNovedades.add(producto)
                    }
                }
                onComplete(listaNovedades)
            }
            .addOnFailureListener { exception ->
                Log.e("Error", "Error al obtener novedades: $exception")
                onComplete(emptyList()) // Devolver una lista vacía en caso de error
            }
    }

    fun obtenerProductosTopVentas(onComplete: (List<Producto>) -> Unit) {
        val listaProductos = mutableListOf<Producto>()

        referenceProductos.orderBy("veces_pedido",Query.Direction.DESCENDING) // Ordenar por veces_pedido de mayor a menor
            .limit(2) // Limitar la cantidad de resultados a 2
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val idProducto = document.id
                    val nombre = document.getString("nombre")
                    val precio = document.getDouble("precio")
                    val imgUrl = document.getString("imgUrl")
                    val idFamilia = document.getString("idFamilia")

                    if (nombre != null && precio != null && imgUrl != null && idFamilia != null) {
                        val producto =
                            Producto(idProducto, nombre, precio.toDouble(), imgUrl, idFamilia)
                        listaProductos.add(producto)
                    }
                }
                onComplete(listaProductos)
            }
            .addOnFailureListener { exception ->
                Log.e("Error", "Error al obtener productos por veces_pedido: $exception")
                onComplete(emptyList()) // Devolver una lista vacía en caso de error
            }
    }

    fun obtenerProductoPorId(idProducto: String, onComplete: (Producto?) -> Unit) {
        referenceProductos.document(idProducto).get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val idProducto = document.id
                val nombre = document.getString("nombre")
                val precio = document.getDouble("precio")
                val imgUrl = document.getString("imgUrl")
                val idFamilia = document.getString("idFamilia")

                if (nombre != null && precio != null && imgUrl != null && idFamilia != null) {
                    val producto =
                        Producto(idProducto, nombre, precio.toDouble(), imgUrl, idFamilia)
                    onComplete(producto)
                } else {
                    onComplete(null) // Devolver null si falta algún campo
                }
            } else {
                onComplete(null) // Devolver null si no se encuentra ningún documento
            }
        }.addOnFailureListener { exception ->
            Log.d("Error", "$exception")
            onComplete(null) // Devolver null en caso de error
        }
    }

    fun crearPedido(lineasPedidos: ArrayList<LineasPedido>, user: String, onComplete: () -> Unit) {
        val datosPedido: MutableMap<String, Any> = HashMap()
        val fechaActual = Calendar.getInstance().time

        datosPedido["usuario"] = user
        datosPedido["fecha"] = fechaActual
        datosPedido["estado"] = "Pendiente"

        referencePedidos
            .add(datosPedido)
            .addOnSuccessListener { documentReference ->
                val pedidoId = documentReference.id
                val totalTasks = lineasPedidos.size // Solo crear línea por cada línea
                var completedTasks = 0

                // Crear una subcolección "lineas" dentro del pedido
                for (linea in lineasPedidos) {
                    val datosLinea: MutableMap<String, Any> = HashMap()
                    datosLinea["idProducto"] = linea.idProducto
                    datosLinea["cantidad"] = linea.cantidad

                    // Añadir cada línea de pedido como documento en la subcolección "lineas" del pedido creado
                    referencePedidos
                        .document(pedidoId)
                        .collection("lineas")
                        .add(datosLinea)
                        .addOnSuccessListener { lineDocumentReference ->
                            Log.d("Linea", "Linea creada con ID: ${lineDocumentReference.id} en pedido con ID: $pedidoId")
                            completedTasks++
                            if (completedTasks == totalTasks) {
                                Utils.CONTROLAR_PEDIDOS.clear()
                                onComplete()
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.e("Error", "Error al crear la línea en pedido con ID: $pedidoId, excepción: $exception")
                            completedTasks++
                            if (completedTasks == totalTasks) {
                                Utils.CONTROLAR_PEDIDOS.clear()
                                onComplete()
                            }
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Error", "Error al crear el pedido: $exception")
                onComplete()
            }
    }

    //Para un pedido individual
    fun crearPedidoLinea(lineaPedido: LineasPedido, user: String) {
        val datosPedido: MutableMap<String, Any> = HashMap()
        val fechaActual = Calendar.getInstance().time

        datosPedido["usuario"] = user
        datosPedido["fecha"] = fechaActual
        datosPedido["estado"] = "Pendiente"

        referencePedidos
            .add(datosPedido)
            .addOnSuccessListener { documentReference ->
                val pedidoId = documentReference.id

                // Crear una subcolección "lineas" dentro del pedido
                val datosLinea: MutableMap<String, Any> = HashMap()
                datosLinea["idProducto"] = lineaPedido.idProducto
                datosLinea["cantidad"] = lineaPedido.cantidad

                // Añadir la línea de pedido como documento en la subcolección "lineas" del pedido creado
                referencePedidos
                    .document(pedidoId)
                    .collection("lineas")
                    .add(datosLinea)
                    .addOnSuccessListener { lineDocumentReference ->
                        Log.d("Linea", "Linea creada con ID: ${lineDocumentReference.id} en pedido con ID: $pedidoId")
                    }
                    .addOnFailureListener { exception ->
                        Log.e("Error", "Error al crear la línea en pedido con ID: $pedidoId, excepción: $exception")
                    }
            }
            .addOnFailureListener { exception ->
                Log.e("Error", "Error al crear el pedido: $exception")
            }
    }

    fun obtenerPedidosPendientes(user: String, onComplete: (List<Pedido>) -> Unit) {
        val listaPedidos = mutableListOf<Pedido>()
        referencePedidos
            .whereEqualTo("usuario", user)
            .whereEqualTo("estado", "Pendiente")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val idPedido = document.id
                    val estado = document.getString("estado")
                    val fecha = document.getDate("fecha")

                    if (estado != null && fecha != null) {
                        val pedido = Pedido(idPedido, user, estado, fecha)
                        listaPedidos.add(pedido)
                    }
                }
                onComplete(listaPedidos)
            }
            .addOnFailureListener { exception ->
                Log.d("Error", "$exception")
                onComplete(emptyList()) // Devolver una lista vacía en caso de error
            }
    }

    fun obtenerPedidosFinalizados(user: String, onComplete: (List<Pedido>) -> Unit) {
        val listaPedidos = mutableListOf<Pedido>()
        referencePedidos
            .whereEqualTo("usuario", user)
            .whereEqualTo("estado", "Finalizado")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val idPedido = document.id
                    val estado = document.getString("estado")
                    val fecha = document.getDate("fecha")

                    if (estado != null && fecha != null) {
                        val pedido = Pedido(idPedido, user, estado, fecha)
                        listaPedidos.add(pedido)
                    }
                }
                onComplete(listaPedidos)
            }
            .addOnFailureListener { exception ->
                Log.d("Error", "$exception")
                onComplete(emptyList()) // Devolver una lista vacía en caso de error
            }
    }

    fun borrarPedidoPorId(pedidoId: String, onComplete: (Boolean) -> Unit) {
        // Obtener la referencia al documento del pedido por su ID
        referencePedidos.document(pedidoId)
            .delete()
            .addOnSuccessListener {
                Log.d("Eliminar Pedido", "Pedido eliminado con éxito con ID: $pedidoId")
                onComplete(true)
            }
            .addOnFailureListener { exception ->
                Log.e("Error", "Error al eliminar el pedido con ID: $pedidoId, excepción: $exception")
                onComplete(false)
            }
    }

    fun obtenerLineasDePedido(pedidoId: String, onComplete: (List<LineasPedido>) -> Unit) {
        val lineasPedido = mutableListOf<LineasPedido>()

        referencePedidos.document(pedidoId)
            .collection("lineas")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val idProducto = document.getString("idProducto")
                    val cantidad = document.getLong("cantidad")?.toInt()
                    val lineaPedido = LineasPedido(idProducto!!, cantidad!!)
                    lineasPedido.add(lineaPedido)
                }
                onComplete(lineasPedido)
            }
            .addOnFailureListener { exception ->
                Log.e("Error", "Error al obtener las líneas del pedido: $exception")
                onComplete(emptyList()) // Devolver una lista vacía en caso de error
            }
    }

}