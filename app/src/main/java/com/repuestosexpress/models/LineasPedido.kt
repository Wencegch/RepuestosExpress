package com.repuestosexpress.models

class LineasPedido {
    private var _idLineaPedido: String = ""
    private var _idProducto: String = ""
    private var _cantidad: Int = 0
    private var _precio: Double = 0.0

    constructor(idLineaPedido: String, idProducto: String, cantidad: Int, precio: Double){
        _idLineaPedido = idLineaPedido
        _idProducto = idProducto
        _cantidad = cantidad
        _precio = precio
    }

    constructor(idProducto: String, cantidad: Int, precio: Double){
        _idProducto = idProducto
        _cantidad = cantidad
        _precio = precio
    }

    var idLineaPedido: String
        get() = _idLineaPedido
        set(value){
            _idLineaPedido = value
        }

    var idProducto: String
        get() = _idProducto
        set(value){
            _idProducto = value
        }

    var cantidad: Int
        get() = _cantidad
        set(value){
            _cantidad = value
        }

    var precio: Double
        get() = _precio
        set(value){
            _precio = value
        }
}