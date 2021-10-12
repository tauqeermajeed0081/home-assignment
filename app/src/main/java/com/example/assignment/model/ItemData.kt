package com.example.assignment.model

class ItemData {
    var id: String? = null
    var name: String? = null

    override fun toString(): String {
        return name!!
    }
}