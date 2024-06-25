package com.repuestosexpress.utils

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.repuestosexpress.R
import com.repuestosexpress.models.Familia
import com.repuestosexpress.models.LineasPedido
import com.repuestosexpress.models.Pedido
import com.repuestosexpress.models.Producto
import java.util.Calendar

/**
 * Clase utilitaria para interactuar con la base de datos Firestore de Firebase.
 * Proporciona métodos para obtener, crear y gestionar datos relacionados con las familias, productos y pedidos.
 */
class Firebase {
    // Referencias a las colecciones de Firestore
    private var referenceFamilias = FirebaseFirestore.getInstance().collection("Familias")
    private var referenceProductos = FirebaseFirestore.getInstance().collection("Productos")
    private var referencePedidos = FirebaseFirestore.getInstance().collection("Pedidos")
    private var referenceUsuarios = FirebaseFirestore.getInstance().collection("Usuarios")

    /**
     * Recupera todas las familias de productos almacenadas en Firestore.
     *
     * @param onComplete Callback que se llama cuando se completa la operación de recuperación de familias.
     *                   Recibe como argumento una lista de objetos de tipo [Familia].
     */
    fun obtenerFamilias(onComplete: (List<Familia>) -> Unit) {
        // Lista mutable para almacenar las familias recuperadas
        val listaFamilias = mutableListOf<Familia>()

        // Obtener todos los documentos de la colección "Familias" en Firestore
        referenceFamilias.whereEqualTo("eliminado", false)
            .get().addOnSuccessListener { querySnapshot ->
            // Iterar sobre los documentos recuperados
            for (document in querySnapshot.documents) {
                // Extraer campos del documento
                val id = document.id
                val nombre = document.getString("nombre")
                val info = document.getString("info")
                val imgUrl = document.getString("imgUrl")

                // Verificar si los campos necesarios no son nulos
                if (nombre != null && info != null && imgUrl != null) {
                    // Crear un objeto Familia y agregarlo a la lista
                    val familia = Familia(id, nombre, info, imgUrl)
                    listaFamilias.add(familia)
                }
            }
            // Llamar al callback onComplete con la lista de familias recuperadas
            onComplete(listaFamilias)
        }.addOnFailureListener { exception ->
            // En caso de error, registrar el error y llamar al callback con una lista vacía
            Log.d("Error", "$exception")
            onComplete(emptyList())
        }
    }


    /**
     * Recupera todos los productos pertenecientes a una familia específica almacenados en Firestore.
     *
     * @param idFamilia Identificador de la familia de productos de la cual se desean recuperar los productos.
     * @param onComplete Callback que se llama cuando se completa la operación de recuperación de productos.
     *                   Recibe como argumento una lista de objetos de tipo [Producto].
     */
    fun obtenerProductosFamilia(idFamilia: String, onComplete: (List<Producto>) -> Unit) {
        // Lista mutable para almacenar los productos recuperados
        val listaProductos = mutableListOf<Producto>()

        // Consulta Firestore para recuperar productos de la familia especificada que no han sido eliminados
        referenceProductos.whereEqualTo("idFamilia", idFamilia)
            .whereEqualTo("eliminado", false)
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Iterar sobre los documentos recuperados
                for (document in querySnapshot.documents) {
                    // Extraer campos del documento
                    val idFirebase = document.id
                    val nombre = document.getString("nombre")
                    val precio = document.getDouble("precio")
                    val imgUrl = document.getString("imgUrl")

                    // Verificar si los campos necesarios no son nulos
                    if (nombre != null && precio != null && imgUrl != null) {
                        // Crear un objeto Producto y agregarlo a la lista
                        val producto = Producto(idFirebase, nombre, precio.toDouble(), imgUrl, idFamilia)
                        listaProductos.add(producto)
                    }
                }
                // Llamar al callback onComplete con la lista de productos recuperados
                onComplete(listaProductos)
            }.addOnFailureListener { exception ->
                // En caso de error, registrar el error y llamar al callback con una lista vacía
                Log.d("Error", "$exception")
                onComplete(emptyList())
            }
    }

    /**
     * Recupera todos los productos sugeridos almacenados en Firestore.
     *
     * @param onComplete Callback que se llama cuando se completa la operación de recuperación de productos sugeridos.
     *                   Recibe como argumento una lista de objetos de tipo [Producto].
     */
    fun obtenerProductosSugeridos(onComplete: (List<Producto>) -> Unit) {
        // Lista mutable para almacenar los productos sugeridos recuperados
        val listaProductosSugeridos = mutableListOf<Producto>()

        // Consulta Firestore para recuperar productos marcados como sugeridos
        referenceProductos.whereEqualTo("sugerencias", true)
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Iterar sobre los documentos recuperados
                for (document in querySnapshot.documents) {
                    // Extraer campos del documento
                    val idProducto = document.id
                    val nombre = document.getString("nombre")
                    val precio = document.getDouble("precio")
                    val imgUrl = document.getString("imgUrl")
                    val idFamilia = document.getString("idFamilia")

                    // Verificar si los campos necesarios no son nulos
                    if (nombre != null && precio != null && imgUrl != null && idFamilia != null) {
                        // Crear un objeto Producto y agregarlo a la lista de productos sugeridos
                        val producto = Producto(idProducto, nombre, precio.toDouble(), imgUrl, idFamilia)
                        listaProductosSugeridos.add(producto)
                    }
                }
                // Llamar al callback onComplete con la lista de productos sugeridos recuperados
                onComplete(listaProductosSugeridos)
            }
            .addOnFailureListener { exception ->
                // En caso de error, registrar el error y llamar al callback con una lista vacía
                Log.e("Error", "Error al obtener productos sugeridos: $exception")
                onComplete(emptyList())
            }
    }


    /**
     * Recupera los últimos productos añadidos almacenados en Firestore, ordenados por fecha de forma descendente.
     * Limita la cantidad de resultados a 4.
     *
     * @param onComplete Callback que se llama cuando se completa la operación de recuperación de productos nuevos.
     *                   Recibe como argumento una lista de objetos de tipo [Producto].
     */
    fun obtenerProductosNovedades(onComplete: (List<Producto>) -> Unit) {
        // Lista mutable para almacenar los productos nuevos recuperados
        val listaNovedades = mutableListOf<Producto>()

        // Consulta Firestore para recuperar productos ordenados por fecha de forma descendente y limitados a 4 resultados
        referenceProductos
            .orderBy("fecha", Query.Direction.DESCENDING)
            .limit(4)
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Iterar sobre los documentos recuperados
                for (document in querySnapshot.documents) {
                    // Extraer campos del documento
                    val idProducto = document.id
                    val nombre = document.getString("nombre")
                    val precio = document.getDouble("precio")
                    val imgUrl = document.getString("imgUrl")
                    val idFamilia = document.getString("idFamilia")
                    val eliminado = document.getBoolean("eliminado")

                    // Verificar si los campos necesarios no son nulos y el producto no está eliminado
                    if (nombre != null && precio != null && imgUrl != null && idFamilia != null && eliminado == false) {
                        // Crear un objeto Producto y agregarlo a la lista de productos nuevos
                        val producto = Producto(idProducto, nombre, precio.toDouble(), imgUrl, idFamilia)
                        listaNovedades.add(producto)
                    }
                }
                // Llamar al callback onComplete con la lista de productos nuevos recuperados
                onComplete(listaNovedades)
            }
            .addOnFailureListener { exception ->
                // En caso de error, registrar el error y llamar al callback con una lista vacía
                Log.e("Error", "Error al obtener novedades: $exception")
                onComplete(emptyList())
            }
    }

    /**
     * Recupera los productos más vendidos almacenados en Firestore, ordenados por el número de veces que han sido pedidos de forma descendente.
     * Limita la cantidad de resultados a 2.
     *
     * @param onComplete Callback que se llama cuando se completa la operación de recuperación de productos más vendidos.
     *                   Recibe como argumento una lista de objetos de tipo [Producto].
     */
    fun obtenerProductosTopVentas(onComplete: (List<Producto>) -> Unit) {
        // Lista mutable para almacenar los productos más vendidos recuperados
        val listaProductos = mutableListOf<Producto>()

        // Consulta Firestore para recuperar productos no eliminados ordenados por veces que han sido pedidos de forma descendente y limitados a 2 resultados
        referenceProductos
            .whereEqualTo("eliminado", false)
            .orderBy("veces_pedido", Query.Direction.DESCENDING)
            .limit(2)
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Iterar sobre los documentos recuperados
                for (document in querySnapshot.documents) {
                    // Extraer campos del documento
                    val idProducto = document.id
                    val nombre = document.getString("nombre")
                    val precio = document.getDouble("precio")
                    val imgUrl = document.getString("imgUrl")
                    val idFamilia = document.getString("idFamilia")

                    // Verificar si los campos necesarios no son nulos
                    if (nombre != null && precio != null && imgUrl != null && idFamilia != null) {
                        // Crear un objeto Producto y agregarlo a la lista de productos más vendidos
                        val producto = Producto(idProducto, nombre, precio.toDouble(), imgUrl, idFamilia)
                        listaProductos.add(producto)
                    }
                }
                // Llamar al callback onComplete con la lista de productos más vendidos recuperados
                onComplete(listaProductos)
            }
            .addOnFailureListener { exception ->
                // En caso de error, registrar el error y llamar al callback con una lista vacía
                Log.e("Error", "Error al obtener productos por veces_pedido: $exception")
                onComplete(emptyList())
            }
    }

    /**
     * Recupera un producto específico por su ID almacenado en Firestore.
     *
     * @param idProducto ID del producto que se desea recuperar.
     * @param onComplete Callback que se llama cuando se completa la operación de recuperación del producto.
     *                   Recibe como argumento el objeto Producto recuperado o null si no se encuentra el producto o hay un error.
     */
    fun obtenerProductoPorId(idProducto: String, onComplete: (Producto?) -> Unit) {
        // Consulta Firestore para recuperar el documento del producto por su ID
        referenceProductos.document(idProducto).get().addOnSuccessListener { document ->
            // Verificar si el documento existe y no es nulo
            if (document != null && document.exists()) {
                // Extraer campos del documento
                val idProducto = document.id
                val nombre = document.getString("nombre")
                val precio = document.getDouble("precio")
                val imgUrl = document.getString("imgUrl")
                val idFamilia = document.getString("idFamilia")

                // Verificar si los campos necesarios no son nulos
                if (nombre != null && precio != null && imgUrl != null && idFamilia != null) {
                    // Crear un objeto Producto con los datos recuperados y llamar al callback con este objeto
                    val producto = Producto(idProducto, nombre, precio.toDouble(), imgUrl, idFamilia)
                    onComplete(producto)
                } else {
                    // Llamar al callback con null si falta algún campo necesario en el documento
                    onComplete(null)
                }
            } else {
                // Llamar al callback con null si no se encuentra ningún documento con el ID dado
                onComplete(null)
            }
        }.addOnFailureListener { exception ->
            // En caso de error, registrar el error y llamar al callback con null
            Log.d("Error", "$exception")
            onComplete(null)
        }
    }

    /**
     * Crea un nuevo pedido en Firestore junto con sus líneas de pedido correspondientes.
     *
     * @param lineasPedidos Lista de objetos LineasPedido que representan las líneas de pedido que se agregarán al pedido.
     * @param user Usuario asociado al pedido.
     * @param direccion Dirección de envío del pedido.
     * @param metodoPago Método de pago del pedido.
     * @param onComplete Callback que se llama cuando se completa la creación del pedido y todas sus líneas de pedido.
     *                   No recibe argumentos.
     */
    fun crearPedido(lineasPedidos: ArrayList<LineasPedido>, user: String, direccion: String, metodoPago: String, onComplete: () -> Unit) {
        // Crear un mapa mutable para almacenar los datos del pedido
        val datosPedido: MutableMap<String, Any> = HashMap()
        // Obtener la fecha y hora actuales
        val fechaActual = Calendar.getInstance().time

        // Agregar los datos del pedido al mapa
        datosPedido["usuario"] = user
        datosPedido["fecha"] = fechaActual
        datosPedido["estado"] = "Pendiente"
        datosPedido["direccion"] = direccion
        datosPedido["metodoPago"] = metodoPago

        // Agregar el pedido a la colección de pedidos en Firestore
        referencePedidos
            .add(datosPedido)
            .addOnSuccessListener { documentReference ->
                // Obtener el ID del pedido recién creado
                val pedidoId = documentReference.id
                // Contador para realizar un seguimiento del número total de tareas completadas
                val totalTasks = lineasPedidos.size
                var completedTasks = 0

                // Recorrer la lista de líneas de pedido y crear cada línea de pedido en Firestore
                for (linea in lineasPedidos) {
                    // Crear un mapa mutable para almacenar los datos de la línea de pedido
                    val datosLinea: MutableMap<String, Any> = HashMap()
                    // Agregar los datos de la línea de pedido al mapa
                    datosLinea["idProducto"] = linea.idProducto
                    datosLinea["cantidad"] = linea.cantidad

                    // Obtener el producto asociado a la línea de pedido y agregar su precio a los datos de la línea de pedido
                    obtenerProductoPorId(linea.idProducto) { producto ->
                        // Verificar si se encontró el producto
                        if (producto != null) {
                            // Agregar el precio del producto a los datos de la línea de pedido
                            datosLinea["precio"] = producto.precio

                            // Agregar la línea de pedido como documento en la subcolección "lineas" del pedido creado
                            referencePedidos
                                .document(pedidoId)
                                .collection("lineas")
                                .add(datosLinea)
                                .addOnSuccessListener { lineDocumentReference ->
                                    // Registro de la creación exitosa de la línea de pedido
                                    Log.d(
                                        "Linea",
                                        "Linea creada con ID: ${lineDocumentReference.id} en pedido con ID: $pedidoId"
                                    )
                                    // Incrementar el contador de tareas completadas
                                    completedTasks++
                                    // Verificar si se han completado todas las tareas
                                    if (completedTasks == totalTasks) {
                                        // Limpiar la lista de pedidos en caso de éxito
                                        Utils.LISTA_PEDIDOS.clear()
                                        // Llamar al callback onComplete una vez completadas todas las tareas
                                        onComplete()
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    // Registro de error al crear la línea de pedido
                                    Log.e(
                                        "Error",
                                        "Error al crear la línea en pedido con ID: $pedidoId, excepción: $exception"
                                    )
                                    // Incrementar el contador de tareas completadas
                                    completedTasks++
                                    // Verificar si se han completado todas las tareas
                                    if (completedTasks == totalTasks) {
                                        // Limpiar la lista de pedidos en caso de éxito
                                        Utils.LISTA_PEDIDOS.clear()
                                        // Llamar al callback onComplete una vez completadas todas las tareas
                                        onComplete()
                                    }
                                }
                        } else {
                            // Registro de error si no se encontró el producto
                            Log.e("Error", "El producto no se encontró.")
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Registro de error al crear el pedido principal
                Log.e("Error", "Error al crear el pedido: $exception")
                // Llamar al callback onComplete en caso de error
                onComplete()
            }
    }

    /**
     * Crea un nuevo pedido en Firestore con una sola línea de pedido.
     *
     * @param lineaPedido Objeto LineasPedido que representa la línea de pedido que se agregará al pedido.
     * @param user Usuario asociado al pedido.
     * @param direccion Dirección de envío del pedido.
     * @param metodoPago Método de pago del pedido.
     */
    fun crearPedidoLinea(lineaPedido: LineasPedido, user: String, direccion: String, metodoPago: String) {
        // Crear un mapa mutable para almacenar los datos del pedido
        val datosPedido: MutableMap<String, Any> = HashMap()
        // Obtener la fecha y hora actuales
        val fechaActual = Calendar.getInstance().time

        // Agregar los datos del pedido al mapa
        datosPedido["usuario"] = user
        datosPedido["fecha"] = fechaActual
        datosPedido["estado"] = "Pendiente"
        datosPedido["direccion"] = direccion
        datosPedido["metodoPago"] = metodoPago

        // Agregar el pedido a la colección de pedidos en Firestore
        referencePedidos
            .add(datosPedido)
            .addOnSuccessListener { documentReference ->
                // Obtener el ID del pedido recién creado
                val pedidoId = documentReference.id

                // Crear un mapa mutable para almacenar los datos de la línea de pedido
                val datosLinea: MutableMap<String, Any> = HashMap()
                // Agregar los datos de la línea de pedido al mapa
                datosLinea["idProducto"] = lineaPedido.idProducto
                datosLinea["cantidad"] = lineaPedido.cantidad

                // Obtener el producto asociado a la línea de pedido y agregar su precio a los datos de la línea de pedido
                obtenerProductoPorId(lineaPedido.idProducto) { producto ->
                    // Verificar si se encontró el producto
                    if (producto != null) {
                        // Agregar el precio del producto a los datos de la línea de pedido
                        datosLinea["precio"] = producto.precio

                        // Añadir la línea de pedido como documento en la subcolección "lineas" del pedido creado
                        referencePedidos
                            .document(pedidoId)
                            .collection("lineas")
                            .add(datosLinea)
                            .addOnSuccessListener { lineDocumentReference ->
                                // Registro de la creación exitosa de la línea de pedido
                                Log.d("Linea", "Linea creada con ID: ${lineDocumentReference.id} en pedido con ID: $pedidoId")
                            }
                            .addOnFailureListener { exception ->
                                // Registro de error al crear la línea de pedido
                                Log.e("Error", "Error al crear la línea en pedido con ID: $pedidoId, excepción: $exception")
                            }
                    } else {
                        // Registro de error si no se encontró el producto
                        Log.e("Error", "El producto no se encontró.")
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Registro de error al crear el pedido principal
                Log.e("Error", "Error al crear el pedido: $exception")
            }
    }

    /**
     * Obtiene la lista de pedidos pendientes asociados a un usuario específico.
     *
     * @param user Usuario para el cual se desea obtener los pedidos pendientes.
     * @param onComplete Función de retorno que se llama cuando se completa la operación.
     *                   Recibe como parámetro la lista de pedidos pendientes o una lista vacía si ocurre un error.
     */
    fun obtenerPedidosPendientes(user: String, onComplete: (List<Pedido>) -> Unit) {
        // Lista mutable para almacenar los pedidos pendientes
        val listaPedidos = mutableListOf<Pedido>()

        // Consultar la colección de pedidos en Firestore para obtener los pedidos pendientes del usuario
        referencePedidos
            .whereEqualTo("usuario", user) // Filtrar por usuario
            .whereEqualTo("estado", "Pendiente") // Filtrar por estado "Pendiente"
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Iterar sobre los documentos de la consulta
                for (document in querySnapshot.documents) {
                    // Obtener los campos del documento
                    val idPedido = document.id
                    val estado = document.getString("estado")
                    val fecha = document.getDate("fecha")
                    val direccion = document.getString("direccion")
                    val metodoPago = document.getString("metodoPago")

                    // Verificar si los campos requeridos no son nulos
                    if (estado != null && fecha != null && direccion != null && metodoPago != null) {
                        // Crear un objeto Pedido y agregarlo a la lista de pedidos pendientes
                        val pedido = Pedido(idPedido, user, estado, fecha, direccion, metodoPago)
                        listaPedidos.add(pedido)
                    }
                }
                // Llamar a la función onComplete con la lista de pedidos pendientes
                onComplete(listaPedidos)
            }
            .addOnFailureListener { exception ->
                // Registro de errores en caso de falla en la consulta
                Log.d("Error", "$exception")
                // Llamar a la función onComplete con una lista vacía en caso de error
                onComplete(emptyList())
            }
    }

    /**
     * Obtiene la lista de pedidos finalizados asociados a un usuario específico.
     *
     * @param user Usuario para el cual se desea obtener los pedidos finalizados.
     * @param onComplete Función de retorno que se llama cuando se completa la operación.
     *                   Recibe como parámetro la lista de pedidos finalizados o una lista vacía si ocurre un error.
     */
    fun obtenerPedidosFinalizados(user: String, onComplete: (List<Pedido>) -> Unit) {
        // Lista mutable para almacenar los pedidos finalizados
        val listaPedidos = mutableListOf<Pedido>()

        // Consultar la colección de pedidos en Firestore para obtener los pedidos finalizados del usuario
        referencePedidos
            .whereEqualTo("usuario", user) // Filtrar por usuario
            .whereEqualTo("estado", "Finalizado") // Filtrar por estado "Finalizado"
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Iterar sobre los documentos de la consulta
                for (document in querySnapshot.documents) {
                    // Obtener los campos del documento
                    val idPedido = document.id
                    val estado = document.getString("estado")
                    val fecha = document.getDate("fecha")
                    val direccion = document.getString("direccion")
                    val metodoPago = document.getString("metodoPago")

                    // Verificar si los campos requeridos no son nulos
                    if (estado != null && fecha != null && direccion != null && metodoPago != null) {
                        // Crear un objeto Pedido y agregarlo a la lista de pedidos finalizados
                        val pedido = Pedido(idPedido, user, estado, fecha, direccion, metodoPago)
                        listaPedidos.add(pedido)
                    }
                }
                // Llamar a la función onComplete con la lista de pedidos finalizados
                onComplete(listaPedidos)
            }
            .addOnFailureListener { exception ->
                // Registro de errores en caso de falla en la consulta
                Log.d("Error", "$exception")
                // Llamar a la función onComplete con una lista vacía en caso de error
                onComplete(emptyList())
            }
    }

    /**
     * Borra un pedido y todas sus líneas asociadas por su ID.
     *
     * @param pedidoId ID del pedido que se desea borrar.
     * @param onComplete Función de retorno que se llama cuando se completa la operación.
     *                   Recibe como parámetro un valor booleano que indica si la operación se realizó con éxito o no.
     */
    fun borrarPedidoPorId(pedidoId: String, onComplete: (Boolean) -> Unit) {
        // Obtener y eliminar todas las líneas de pedido en la subcolección "lineas"
        referencePedidos.document(pedidoId).collection("lineas")
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Inicializar un lote de escritura de Firestore
                val batch = FirebaseFirestore.getInstance().batch()
                // Iterar sobre los documentos de la consulta para eliminarlos del lote
                for (document in querySnapshot.documents) {
                    batch.delete(document.reference)
                }

                // Aplicar el lote de eliminación de líneas de pedido
                batch.commit()
                    .addOnSuccessListener {
                        // Una vez que las líneas de pedido están eliminadas, eliminar el documento del pedido
                        referencePedidos.document(pedidoId).delete()
                            .addOnSuccessListener {
                                // Registro del éxito de la operación
                                Log.d("Eliminar Pedido", "Pedido y subcolecciones eliminados con éxito con ID: $pedidoId")
                                // Llamar a la función onComplete con éxito
                                onComplete(true)
                            }
                            .addOnFailureListener { exception ->
                                // Registro de errores en caso de fallo al eliminar el pedido
                                Log.e("Error", "Error al eliminar el pedido con ID: $pedidoId, excepción: $exception")
                                // Llamar a la función onComplete con fallo
                                onComplete(false)
                            }
                    }
                    .addOnFailureListener { exception ->
                        // Registro de errores en caso de fallo al aplicar el lote de eliminación
                        Log.e("Error", "Error al eliminar subcolecciones del pedido con ID: $pedidoId, excepción: $exception")
                        // Llamar a la función onComplete con fallo
                        onComplete(false)
                    }
            }
            .addOnFailureListener { exception ->
                // Registro de errores en caso de fallo al obtener las subcolecciones del pedido
                Log.e("Error", "Error al obtener subcolecciones del pedido con ID: $pedidoId, excepción: $exception")
                // Llamar a la función onComplete con fallo
                onComplete(false)
            }
    }

    /**
     * Obtiene las líneas de un pedido por su ID.
     *
     * @param pedidoId ID del pedido del cual se desean obtener las líneas.
     * @param onComplete Función de retorno que se llama cuando se completa la operación.
     *                   Recibe como parámetro una lista de objetos LineasPedido que representan las líneas del pedido.
     */
    fun obtenerLineasDePedido(pedidoId: String, onComplete: (List<LineasPedido>) -> Unit) {
        // Lista mutable para almacenar las líneas de pedido recuperadas
        val lineasPedido = mutableListOf<LineasPedido>()

        // Consultar la subcolección "lineas" del pedido especificado por su ID
        referencePedidos.document(pedidoId)
            .collection("lineas")
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Iterar sobre los documentos de la consulta para recuperar los datos de las líneas de pedido
                for (document in querySnapshot.documents) {
                    // Obtener los campos relevantes de cada documento
                    val idProducto = document.getString("idProducto")
                    val cantidad = document.getLong("cantidad")?.toInt()
                    val precio = document.getDouble("precio")

                    // Verificar si los campos requeridos no son nulos antes de crear un objeto LineasPedido
                    if (idProducto != null && cantidad != null && precio != null) {
                        // Crear un objeto LineasPedido con los datos recuperados y agregarlo a la lista
                        val lineaPedido = LineasPedido(idProducto, cantidad, precio)
                        lineasPedido.add(lineaPedido)
                    }
                }
                // Llamar a la función onComplete con la lista de líneas de pedido recuperadas
                onComplete(lineasPedido)
            }
            .addOnFailureListener { exception ->
                // Registro de errores en caso de fallo al obtener las líneas del pedido
                Log.e("Error", "Error al obtener las líneas del pedido: $exception")
                // Llamar a la función onComplete con una lista vacía en caso de error
                onComplete(emptyList())
            }
    }

    /**
     * Crea un nuevo usuario en la base de datos de Firestore.
     *
     * @param context Contexto de la aplicación.
     * @param email Correo electrónico del usuario que se va a crear.
     * @param nombre Nombre del usuario que se va a crear.
     * @param uid Identificador único del usuario proporcionado por Firebase Authentication.
     */
    fun crearUsuario(context: Context, email: String, nombre: String, uid: String) {
        // Verificar si el usuario ya existe en la base de datos
        referenceUsuarios.whereEqualTo("email", email).get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    // Si el usuario no existe, crearlo
                    val datosUsuario: MutableMap<String, Any> = HashMap()
                    datosUsuario["email"] = email
                    datosUsuario["nombre"] = nombre
                    datosUsuario["uid"] = uid
                    datosUsuario["administrador"] = false

                    referenceUsuarios.add(datosUsuario).addOnSuccessListener { documentReference ->
                        val id: String = documentReference.id
                        Utils.Toast(context, context.getString(R.string.user) +" " +  nombre + " " + context.getString(R.string.created))
                        Log.i("Crear usuario", "Exitoso. ID del usuario: $id")
                    }.addOnFailureListener { exception ->
                        Log.e("Error", "$exception")
                    }
                } else {
                    // Si el usuario ya existe, simplemente iniciar sesión
                    Log.i("Crear usuario", "El usuario ya existe en la base de datos.")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Error", "Error al verificar el usuario: $exception")
            }
    }

}