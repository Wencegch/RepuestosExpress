package com.repuestosexpress.models

import java.util.Date

class Pedido {
    private var _id: String = ""
    private var _usuario: String = ""
    private var _estado: String = ""
    private var _idPedido: String = ""
    private var _fecha: Date

    constructor(id: String, usuario: String, estado: String, idPedido: String, fecha: Date) {
        _id = id
        _usuario = usuario
        _estado = estado
        _idPedido = idPedido
        _fecha = fecha
    }

    constructor(usuario: String, estado: String, idPedido: String, fecha: Date) {
        _usuario = usuario
        _estado = estado
        _idPedido = idPedido
        _fecha = fecha
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

    var fecha: Date
        get() = _fecha
        set(value) {
            _fecha = value
        }

}