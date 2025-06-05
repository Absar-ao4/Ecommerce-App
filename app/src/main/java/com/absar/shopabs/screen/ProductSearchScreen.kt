package com.absar.shopabs.screen

import androidx.compose.runtime.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.absar.shopabs.model.ProductModel
import com.absar.shopabs.components.ProductItemView
import com.google.firebase.Firebase
import com.google.firebase.database.*
import com.google.firebase.firestore.firestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductSearchScreen(
    onBack: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    val productList = remember { mutableStateOf<List<ProductModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true
        Firebase.firestore.collection("data").document("stock")
            .collection("products")
            .get()
            .addOnCompleteListener {
                isLoading = false
                if (it.isSuccessful) {
                    val resultList = it.result.documents.mapNotNull { doc ->
                        doc.toObject(ProductModel::class.java)
                    }
                    productList.value = resultList
                }
            }
    }

    val filteredProducts = productList.value.filter {
        it.title.contains(query, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(8.dp)
    ) {
        TextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Search products...", color = Color.White.copy(alpha = 0.5f)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFBE52F2).copy(alpha = 0.15f),
                focusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFBE52F2))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredProducts) { product ->
                    ProductItemView(modifier = Modifier.fillMaxWidth(), product = product)
                }
            }
        }
    }
}
