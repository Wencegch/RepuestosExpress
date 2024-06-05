package com.repuestosexpress.models

import java.io.Serializable
import java.util.Date

class Pedido: Serializable {
    private var _id: String = ""
    private var _usuario: String = ""
    private var _estado: String = ""
    private var _fecha: Date
    private var _direccion: String = ""

    constructor(id: String, usuario: String, estado: String, fecha: Date, direccion: String) {
        _id = id
        _usuario = usuario
        _estado = estado
        _fecha = fecha
        _direccion = direccion
    }

    constructor(usuario: String, estado: String, fecha: Date, direccion: String) {
        _usuario = usuario
        _estado = estado
        _fecha = fecha
        _direccion = direccion
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

    var fecha: Date
        get() = _fecha
        set(value) {
            _fecha = value
        }

    var direccion: String
        get() = _direccion
        set(value) {
            _direccion = value
        }
}