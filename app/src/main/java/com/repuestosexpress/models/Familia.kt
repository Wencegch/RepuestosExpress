package com.repuestosexpress.models

/**
 * Clase que representa una familia de productos.
 * @property _id El identificador único de la familia.
 * @property _nombre El nombre de la familia.
 * @property _info La información adicional sobre la familia.
 * @property _imgUrl La URL de la imagen asociada a la familia.
 * @constructor Crea una nueva instancia de la clase Familia.
 */
class Familia {
    private var _id: String = ""
    private var _nombre: String = ""
    private var _info: String = ""
    private var _imgUrl: String = ""

    /**
     * Constructor primario para crear una instancia de Familia con identificador, nombre, información e URL de imagen.
     * @param id El identificador único de la familia.
     * @param nombre El nombre de la familia.
     * @param info La información adicional sobre la familia.
     * @param imgUrl La URL de la imagen asociada a la familia.
     */
    constructor(id: String, nombre: String, info: String, imgUrl: String) {
        _id = id
        _nombre = nombre
        _info = info
        _imgUrl = imgUrl
    }

    /**
     * Constructor secundario para crear una instancia de Familia con nombre, información e URL de imagen.
     * @param nombre El nombre de la familia.
     * @param info La información adicional sobre la familia.
     * @param imgUrl La URL de la imagen asociada a la familia.
     */
    constructor(nombre: String, info: String, imgUrl: String) {
        _nombre = nombre
        _info = info
        _imgUrl = imgUrl
    }

    /**
     * Propiedad para acceder y modificar el identificador único de la familia.
     */
    var id: String
        get() = _id
        set(value) {
            _id = value
        }

    /**
     * Propiedad para acceder y modificar el nombre de la familia.
     */
    var nombre: String
        get() = _nombre
        set(value) {
            _nombre = value
        }

    /**
     * Propiedad para acceder y modificar la información adicional sobre la familia.
     */
    var info: String
        get() = _info
        set(value) {
            _info = value
        }

    /**
     * Propiedad para acceder y modificar la URL de la imagen asociada a la familia.
     */
    var imgUrl: String
        get() = _imgUrl
        set(value) {
            _imgUrl = value
        }
}
