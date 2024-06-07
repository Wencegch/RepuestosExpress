package com.repuestosexpress.models

import java.io.Serializable
import java.util.Date

/**
 * Clase que representa un pedido.
 * @property _id El identificador único del pedido.
 * @property _usuario El usuario que realizó el pedido.
 * @property _estado El estado actual del pedido.
 * @property _fecha La fecha en la que se realizó el pedido.
 * @property _direccion La dirección de envío del pedido.
 * @constructor Crea una nueva instancia de la clase Pedido.
 */
class Pedido: Serializable {
    private var _id: String = ""
    private var _usuario: String = ""
    private var _estado: String = ""
    private var _fecha: Date
    private var _direccion: String = ""

    /**
     * Constructor primario para crear una instancia de Pedido con todos los parámetros.
     * @param id El identificador único del pedido.
     * @param usuario El usuario que realizó el pedido.
     * @param estado El estado actual del pedido.
     * @param fecha La fecha en la que se realizó el pedido.
     * @param direccion La dirección de envío del pedido.
     */
    constructor(id: String, usuario: String, estado: String, fecha: Date, direccion: String) {
        _id = id
        _usuario = usuario
        _estado = estado
        _fecha = fecha
        _direccion = direccion
    }

    /**
     * Constructor secundario para crear una instancia de Pedido sin el identificador único.
     * @param usuario El usuario que realizó el pedido.
     * @param estado El estado actual del pedido.
     * @param fecha La fecha en la que se realizó el pedido.
     * @param direccion La dirección de envío del pedido.
     */
    constructor(usuario: String, estado: String, fecha: Date, direccion: String) {
        _usuario = usuario
        _estado = estado
        _fecha = fecha
        _direccion = direccion
    }

    /**
     * Propiedad para acceder y modificar el identificador único del pedido.
     */
    var id: String
        get() = _id
        set(value) {
            _id = value
        }

    /**
     * Propiedad para acceder y modificar el usuario que realizó el pedido.
     */
    var usuario: String
        get() = _usuario
        set(value) {
            _usuario = value
        }

    /**
     * Propiedad para acceder y modificar el estado actual del pedido.
     */
    var estado: String
        get() = _estado
        set(value) {
            _estado = value
        }

    /**
     * Propiedad para acceder y modificar la fecha en la que se realizó el pedido.
     */
    var fecha: Date
        get() = _fecha
        set(value) {
            _fecha = value
        }

    /**
     * Propiedad para acceder y modificar la dirección de envío del pedido.
     */
    var direccion: String
        get() = _direccion
        set(value) {
            _direccion = value
        }
}
