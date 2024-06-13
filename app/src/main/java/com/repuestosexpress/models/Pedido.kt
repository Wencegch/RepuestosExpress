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
 * @property _metodoPago El método de pago del pedido.
 * @constructor Crea una nueva instancia de la clase Pedido.
 */
class Pedido: Serializable {
    private var _id: String = ""
    private var _usuario: String = ""
    private var _estado: String = ""
    private var _fecha: Date
    private var _direccion: String = ""
    private var _metodoPago: String = ""

    /**
     * Constructor primario para crear una instancia de Pedido con todos los parámetros.
     * @param id El identificador único del pedido.
     * @param usuario El usuario que realizó el pedido.
     * @param estado El estado actual del pedido.
     * @param fecha La fecha en la que se realizó el pedido.
     * @param direccion La dirección de envío del pedido.
     * @param metodoPago El método de pago del pedido.
     */
    constructor(id: String, usuario: String, estado: String, fecha: Date, direccion: String, metodoPago: String) {
        _id = id
        _usuario = usuario
        _estado = estado
        _fecha = fecha
        _direccion = direccion
        _metodoPago = metodoPago
    }

    /**
     * Constructor secundario para crear una instancia de Pedido sin el identificador único.
     * @param usuario El usuario que realizó el pedido.
     * @param estado El estado actual del pedido.
     * @param fecha La fecha en la que se realizó el pedido.
     * @param direccion La dirección de envío del pedido.
     */
    constructor(usuario: String, estado: String, fecha: Date, direccion: String, metodoPago: String) {
        _usuario = usuario
        _estado = estado
        _fecha = fecha
        _direccion = direccion
        _metodoPago = metodoPago
    }

    /**
     * Getter y Setter para el ID del pedido.
     */
    var id: String
        get() = _id
        set(value) {
            _id = value
        }

    /**
     * Getter y Setter para el usuario que realizó el pedido.
     */
    var usuario: String
        get() = _usuario
        set(value) {
            _usuario = value
        }

    /**
     * Getter y Setter para el estado actual del pedido.
     */
    var estado: String
        get() = _estado
        set(value) {
            _estado = value
        }

    /**
     * Getter y Setter para la fecha en la que se realizó el pedido.
     */
    var fecha: Date
        get() = _fecha
        set(value) {
            _fecha = value
        }

    /**
     * Getter y Setter para la dirección de envío del pedido.
     */
    var direccion: String
        get() = _direccion
        set(value) {
            _direccion = value
        }

    /**
     * Getter y Setter para el método de pago del pedido.
     */
    var metodoPago: String
        get() = _metodoPago
        set(value) {
            _metodoPago = value
        }
}