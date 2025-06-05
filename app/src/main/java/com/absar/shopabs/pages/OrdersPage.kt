package com.absar.shopabs.pages

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.absar.shopabs.components.BackButton
import com.absar.shopabs.components.OrderView

import com.absar.shopabs.model.OrderModel
import com.absar.shopabs.ui.theme.DarkGrey
import com.absar.shopabs.ui.theme.ElectricPurple
import com.absar.shopabs.ui.theme.White
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun OrdersPage() {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val orders = remember { mutableStateListOf<OrderModel>() }
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    LaunchedEffect(userId) {
        if (userId != null) {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("orders")
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, _ ->
                    if (snapshot != null) {
                        orders.clear()
                        for (doc in snapshot.documents) {
                            val order = doc.toObject(OrderModel::class.java)
                            if (order != null) {
                                orders.add(order.copy(id = doc.id))
                            }
                        }
                    }
                }
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DarkGrey,
        topBar = {
//            Spacer(
//                modifier=Modifier.height(36.dp)
//            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkGrey)
                    .statusBarsPadding()
                    .padding(8.dp)
            ) {
                BackButton(
                    onBack = { dispatcher?.onBackPressed() },
                    tint = ElectricPurple
                )
                Text(
                    "Orders",
                    color = White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(orders) { order ->
                OrderView(order = order)
            }
        }
    }
}
