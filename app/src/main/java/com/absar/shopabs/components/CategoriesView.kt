package com.absar.shopabs.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.absar.shopabs.GlobalNavigation
import com.absar.shopabs.model.CategoryModel
import com.absar.shopabs.ui.theme.CardGrey
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.util.Locale.Category

@Composable
fun CategoriesView(modifier: Modifier=Modifier){

    val categoryList = remember {
        mutableStateOf<List<CategoryModel>>(emptyList())
    }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data").document("stock")
            .collection("categories")
            .get().addOnCompleteListener {
                if(it.isSuccessful)
                {
                    val resultList=it.result.documents.mapNotNull { doc->
                        doc.toObject(CategoryModel::class.java)
                    }
                    categoryList.value=resultList
                }
            }
    }

    LazyRow (
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ){
        items(categoryList.value){item ->
            CategoryItem(category=item)
        }
    }

}

@Composable
fun CategoryItem(category: CategoryModel) {
    Card(
        modifier = Modifier
            .size(100.dp)
            .padding(4.dp)
            .clickable {
                GlobalNavigation.navController.navigate("category-products/"+category.id)
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardGrey),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = category.imageUrl,
                contentDescription = category.name,
                modifier = Modifier
                    .size(60.dp)
                    .padding(top = 8.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.name,
                textAlign = TextAlign.Center,
                maxLines = 1,
                color = Color.White,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .fillMaxWidth()
            )
        }
    }
}
