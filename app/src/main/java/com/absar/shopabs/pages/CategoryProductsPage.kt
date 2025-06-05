package com.absar.shopabs.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.absar.shopabs.components.ProductItemView
import com.absar.shopabs.model.CategoryModel
import com.absar.shopabs.model.ProductModel
import com.absar.shopabs.ui.theme.DarkGrey
import com.absar.shopabs.ui.theme.White
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun CategoryProductsPage(modifier: Modifier,categoryId:String)
{
    val productList = remember {
        mutableStateOf<List<ProductModel>>(emptyList())
    }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data").document("stock")
            .collection("products")
            .whereEqualTo("category",categoryId)
            .get().addOnCompleteListener {
                if(it.isSuccessful)
                {
                    val resultList=it.result.documents.mapNotNull { doc->
                        doc.toObject(ProductModel::class.java)
                    }
                    productList.value=resultList
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGrey)
    ) {
        Spacer(modifier=Modifier.height(36.dp))
        Text(
            text = "$categoryId".replaceFirstChar { it.uppercase() },
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp),
            color = White
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            items(productList.value.chunked(2)) { rowItems ->
                Row {
                    rowItems.forEach {
                        ProductItemView(product = it, modifier = Modifier.weight(1f))
                    }
                    if (rowItems.size == 1)
                        Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }

}