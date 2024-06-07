package com.repuestosexpress.models

/**
 * Clase que representa una línea de pedido.
 * @property _idLineaPedido El identificador único de la línea de pedido.
 * @property _idProducto El identificador único del producto asociado a la línea de pedido.
 * @property _cantidad La cantidad del producto en la línea de pedido.
 * @property _precio El precio del producto en la línea de pedido.
 * @constructor Crea una nueva instancia de la clase LineasPedido.
 */
class LineasPedido {
    private var _idLineaPedido: String = ""
    private var _idProducto: String = ""
    private var _cantidad: Int = 0
    private var _precio: Double = 0.0

    /**
     * Constructor primario para crear una instancia de LineasPedido con idProducto, cantidad y precio.
     * @param idProducto El identificador único del producto asociado a la línea de pedido.
     * @param cantidad La cantidad del producto en la línea de pedido.
     * @param precio El precio del producto en la línea de pedido.
     */
    constructor(idProducto: String, cantidad: Int, precio: Double){
        _idProducto = idProducto
        _cantidad = cantidad
        _precio = precio
    }

    /**
     * Constructor secundario para crear una instancia de LineasPedido con idLineaPedido, idProducto, cantidad y precio.
     * @param idLineaPedido El identificador único de la línea de pedido.
     * @param idProducto El identificador único del producto asociado a la línea de pedido.
     * @param cantidad La cantidad del producto en la línea de pedido.
     * @param precio El precio del producto en la línea de pedido.
     */
    constructor(idLineaPedido: String, idProducto: String, cantidad: Int, precio: Double){
        _idLineaPedido = idLineaPedido
        _idProducto = idProducto
        _cantidad = cantidad
        _precio = precio
    }

    /**
     * Propiedad para acceder y modificar el identificador único de la línea de pedido.
     */
    var idLineaPedido: String
        get() = _idLineaPedido
        set(value){
            _idLineaPedido = value
        }

    /**
     * Propiedad para acceder y modificar el identificador único del producto asociado a la línea de pedido.
     */
    var idProducto: String
        get() = _idProducto
        set(value){
            _idProducto = value
        }

    /**
     * Propiedad para acceder y modificar la cantidad del producto en la línea de pedido.
     */
    var cantidad: Int
        get() = _cantidad
        set(value){
            _cantidad = value
        }

    /**
     * Propiedad para acceder y modificar el precio del producto en la línea de pedido.
     */
    var precio: Double
        get() = _precio
        set(value){
            _precio = value
        }
}
