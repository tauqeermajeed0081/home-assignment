package com.example.assignment.model

data class ItemData(
    var id: Int? = -1,
    var name: String? = null
) {

    override fun toString(): String {
        return name!!
    }
}