package com.repuestosexpress.models

class Producto {
    private var _id: String = ""
    private var _nombre: String = ""
    private var _precio: Double = 0.0
    private var _imgUrl: String = ""
    private var _idFamilia: String = ""

    // Constructor sin el ID
    constructor(nombre: String, precio: Double, imgUrl: String, idFamilia: String) {
        _nombre = nombre
        _precio = precio
        _imgUrl = imgUrl
        _idFamilia = idFamilia
    }

    // Constructor con el ID
    constructor(id: String, nombre: String, precio: Double, imgUrl: String, idFamilia: String) {
        _id = id
        _nombre = nombre
        _precio = precio
        _imgUrl = imgUrl
        _idFamilia = idFamilia
    }

    var id: String
        get() = _id
        set(value) {
            _id = value
        }

    var nombre: String
        get() = _nombre
        set(value) {
            _nombre = value
        }

    var precio: Double
        get() = _precio
        set(value) {
            _precio = value
        }

    var imgUrl: String
        get() = _imgUrl
        set(value) {
            _imgUrl = value
        }

    var idFamilia: String
        get() = _idFamilia
        set(value) {
            _idFamilia = value
        }

}

