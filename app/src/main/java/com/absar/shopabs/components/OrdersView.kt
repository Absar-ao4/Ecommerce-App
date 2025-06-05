package com.absar.shopabs.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.absar.shopabs.model.OrderModel
import com.absar.shopabs.ui.theme.CardGrey
import com.absar.shopabs.ui.theme.ElectricPurple
import com.absar.shopabs.ui.theme.White
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun OrderView(
    order: OrderModel,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val productNames = remember { mutableStateMapOf<String, String>() }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = CardGrey),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        LaunchedEffect(order.items.keys) {
            val db = FirebaseFirestore.getInstance()
            order.items.keys.forEach { productId ->
                db.collection("data")
                    .document("stock")
                    .collection("products")
                    .document(productId)
                    .get()
                    .addOnSuccessListener { doc ->
                        val name = doc.getString("title") ?: "Unknown Product"
                        productNames[productId] = name
                    }
            }
        }
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Order",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
                Text(
                    text = order.timestamp.toReadableDate(),
                    style = MaterialTheme.typography.bodySmall,
                    color = White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (!expanded) {
                val firstItem = order.items.entries.firstOrNull()
                if (firstItem != null) {
                    val name = productNames[firstItem.key] ?: "Loading..."
                    Text(
                        text = "$name - Qty: ${firstItem.value}  ...",
                        color = White,
                        fontWeight = FontWeight.SemiBold
                    )
                } else {
                    Text(
                        text = "No items",
                        color = White
                    )
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Address: ${order.address}",
                    color = White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Items:",
                    fontWeight = FontWeight.Bold,
                    color = White
                )
                order.items.forEach { (productId, qty) ->
                    val name = productNames[productId] ?: "Loading..."
                    Text(
                        text = "- $name: $qty",
                        color = White
                    )
                }
            }
        }
    }
}


fun Long.toReadableDate(): String {
    val sdf = java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(this))
}
