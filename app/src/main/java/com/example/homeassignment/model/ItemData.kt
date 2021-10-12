package com.example.homeassignment.model

class ItemData {
    var id: String? = null
    var name: String? = null

    override fun toString(): String {
        return name!!
    }
}