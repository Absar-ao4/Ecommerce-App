package com.absar.shopabs.model

data class OrderModel(
    val items: Map<String, Long> = emptyMap(),
    val address: String = "",
    val timestamp: Long = 0L,
    val id: String = "",
    val title: String = ""
)

