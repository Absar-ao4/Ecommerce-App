package com.absar.shopabs.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.absar.shopabs.AppUtil
import com.absar.shopabs.components.DotsIndicator
import com.absar.shopabs.model.ProductModel
import com.absar.shopabs.ui.theme.DarkGrey
import com.absar.shopabs.ui.theme.ElectricPurple
import com.absar.shopabs.ui.theme.White
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun ProductDetailsPage(modifier: Modifier=Modifier,productId:String){
    var product by  remember {
        mutableStateOf(ProductModel())
    }
    var heart by  rememberSaveable  {
        mutableStateOf(false)
    }
    var context= LocalContext.current

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data").document("stock")
            .collection("products")
            .document(productId).get()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    var result = it.result.toObject(ProductModel::class.java)
                    if(result!=null)
                    {
                        product=result
                    }
                }
            }
    }

    LaunchedEffect(productId) {
        val userid= FirebaseAuth.getInstance().currentUser?.uid
        Firebase.firestore.collection("users")
            .document(userid!!)
            .get()
            .addOnSuccessListener { doc ->
                val favItems = doc.get("favItems") as? Map<String, Long> ?: emptyMap()
                heart = favItems.containsKey(productId)
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGrey)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(DarkGrey)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(36.dp))
            Text(
                text = product.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp),
                color = White
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = modifier) {
                val pagerState = rememberPagerState(0) {
                    product.images.size
                }

                HorizontalPager(state = pagerState, pageSpacing = 24.dp) {
                    AsyncImage(
                        model = product.images.get(it),
                        contentDescription = "Product images",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                    )
                }
                DotsIndicator(
                    pageCount = product.images.size,
                    currentPage = pagerState.currentPage
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "₹" + product.price,
                        fontSize = 14.sp,
                        style = TextStyle(textDecoration = TextDecoration.LineThrough),
                        color = White
                    )
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(
                        text = "₹" + product.actualPrice,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                if(!heart)
                {
                    IconButton(onClick = {
                        heart=!heart
                        AppUtil.addItemToFav(productId, context) }) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Add to Favourite",
                            tint = ElectricPurple
                        )
                    }
                }
                else{
                    IconButton(onClick = {
                        heart=!heart
                        AppUtil.removeFromFav(productId, context) }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Add to Favourite",
                            tint = ElectricPurple
                        )
                    }
                }


            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        AppUtil.addItemToCart(productId, context)
                    },
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, ElectricPurple)
                ) {
                    Text(text = "Add To Cart",color= White)
                }
                Button(
                    onClick = {AppUtil.startPayment(product.actualPrice.toFloat())},
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(ElectricPurple)
                ) {
                    Text(text = "Buy Now")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Product Description :",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.description,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = White
            )

            Spacer(modifier = Modifier.height(16.dp))
            if (product.otherDetails.isNotEmpty()) {
                Text(
                    "Product Specs : ",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = White
                )
                Spacer(modifier = Modifier.height(8.dp))
                product.otherDetails.forEach { (key, value) ->
                    Row(
                        modifier = Modifier.padding(4.dp).fillMaxWidth()
                    ) {
                        Text(
                            "$key : ",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = White
                        )
                        Text(
                            "$value",
                            fontSize = 14.sp,
                            color = White
                        )
                    }
                }


            }
        }
    }
}


@Composable
fun DotsIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    selectedColor: Color = Color.Black,
    unselectedColor: Color = Color.LightGray,
    dotSize: Dp = 10.dp
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) { page ->
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .size(dotSize)
                    .clip(CircleShape)
                    .background(color = if (page == currentPage) selectedColor else unselectedColor)
            )
        }
    }
}
