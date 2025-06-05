package com.absar.shopabs.model

data class UserModel(
    val name:String="",
    val email:String="",
    val uid:String="",
    val address:String="",
    val cartItems: Map<String,Long> = emptyMap(),
    var favItems: Map<String, Long> = emptyMap()
)
