package com.example.shoppinglist.data
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShoppingListModelItem(
    @SerialName("isActive")
    var isActive: Boolean,
    var name: String,
    val price: Int,
    val quantity: Int
)