package com.repuestosexpress.models

class Pedido {
    private var _id: String = ""
    private var _usuario: String = ""
    private var _estado: String = ""
    private var _idPedido: String = ""

    constructor(id: String, usuario: String, estado: String, idPedido: String) {
        _id = id
        _usuario = usuario
        _estado = estado
        _idPedido = idPedido
    }

    constructor(usuario: String, estado: String, idPedido: String) {
        _usuario = usuario
        _estado = estado
        _idPedido = idPedido
    }

    var id: String
        get() = _id
        set(value) {
            _id = value
        }

    var usuario: String
        get() = _usuario
        set(value) {
            _usuario = value
        }

    var estado: String
        get() = _estado
        set(value) {
            _estado = value
        }

    var idPedido: String
        get() = _idPedido
        set(value) {
            _idPedido = value
        }
}